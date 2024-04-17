package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.save

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.manager.device.model.DeviceInfo
import uz.uzkassa.smartpos.core.data.manager.printer.PrinterManager
import uz.uzkassa.smartpos.core.data.manager.printer.print.PrintData
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction.CashTransaction
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.amount.CashAmount
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.print.CashTransactionPrintData
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.save.params.CashTransactionSaveParams
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.dependencies.CashierCashOperationsFeatureArgs
import java.math.BigDecimal
import javax.inject.Inject

internal class CashTransactionSaveRepositoryImpl @Inject constructor(
    private val branchRelationDao: BranchRelationDao,
    private val cashierCashOperationsFeatureArgs: CashierCashOperationsFeatureArgs,
    private val cashTransactionEntityDao: CashTransactionEntityDao,
    private val companyRelationDao: CompanyRelationDao,
    private val deviceInfoManager: DeviceInfoManager,
    private val printerManager: PrinterManager,
    private val shiftReportEntityDao: ShiftReportEntityDao,
    private val userRelationDao: UserRelationDao
) : CashTransactionSaveRepository {
    private var lastTransaction: CashTransaction? = null
    private var lastTransactionPrintData: PrintData? = null
    private var printCount: Int = 0

    private val branchId: Long = cashierCashOperationsFeatureArgs.branchId

    @FlowPreview
    override fun createCashTransaction(params: CashTransactionSaveParams): Flow<Unit> {
        return flowOf(Unit)
            .flatMapConcat {
                val entity = shiftReportEntityDao
                    .getOpenShiftReportEntity().filterNotNull().first()
                getCashTransaction(params)
                    .onEach {
                        cashTransactionEntityDao.upsert(
                            cashTransaction = it,
                            shiftId = entity.id,
                            shiftNumber = entity.fiscalShiftNumber.toLong()
                        )
                    }
                    .map { Pair(params.allowedAmount, it) }
                    .flatMapConcat { pair ->
                        getDetailsForPrintData { branch, company, user ->
                            getPrintData(
                                allowedAmount = pair.first,
                                branch = branch,
                                cashAmount = params.cashAmount,
                                cashTransaction = pair.second,
                                company = company,
                                user = user
                            )
                                .flatMapConcat { printerManager.print(it) }
                                .onEach { printCount += 1 }
                        }
                    }
            }
    }

    @FlowPreview
    override fun printLastCashTransaction(): Flow<Unit> {
        return flowOf(lastTransactionPrintData)
            .onEach { /* duplicate */ }
            .onEach { printCount += 1 }
            .flatMapConcat { if (it != null) printerManager.print(it) else flowOf(Unit) }
    }

    override fun clearTempData(): Flow<Unit> {
        return flowOf(Unit).onEach {
            lastTransaction = null
            lastTransactionPrintData = null
            printCount = 0
        }
    }

    @Suppress("LABEL_NAME_CLASH")
    @FlowPreview
    private fun getCashTransaction(
        params: CashTransactionSaveParams
    ): Flow<CashTransaction> {
        return flowOf(lastTransaction)
            .flatMapConcat {
                shiftReportEntityDao.getOpenShiftReportEntity()
                    .filterNotNull()
                    .flatMapConcat { shift ->
                        val deviceInfo: DeviceInfo = deviceInfoManager.deviceInfo
                        val companyId: Long = companyRelationDao.getRelation().companyEntity.id

                        val element: CashTransaction = params.asCashTransaction(
                            userId = shift.userId,
                            shiftId = shift.id,
                            branchId = branchId,
                            companyId = companyId,
                            shiftNumber = shift.fiscalShiftNumber,
                            terminalModel = deviceInfo.deviceName,
                            terminalSerialNumber = deviceInfo.serialNumber
                        )

                        return@flatMapConcat flowOf(element).onEach { lastTransaction = it }
                    }
            }
    }

    @FlowPreview
    private fun getPrintData(
        allowedAmount: BigDecimal,
        branch: Branch,
        cashAmount: CashAmount,
        cashTransaction: CashTransaction,
        company: Company,
        user: User
    ): Flow<PrintData> {
        return flowOf(lastTransactionPrintData)
            .flatMapConcat { it ->
                return@flatMapConcat if (it != null) flowOf(it)
                else {
                    val printData: PrintData =
                        CashTransactionPrintData(
                            allowedAmount = allowedAmount,
                            branch = branch,
                            cashAmount = cashAmount,
                            cashTransaction = cashTransaction,
                            company = company,
                            user = user
                        )
                    return@flatMapConcat flowOf(printData).onEach { lastTransactionPrintData = it }
                }
            }
    }

    @FlowPreview
    private fun <T> getDetailsForPrintData(
        flow: (Branch, Company, User) -> Flow<T>
    ): Flow<T> {
        val companyFlow: Flow<Company> = flow { emit(companyRelationDao.getRelation().map()) }

        val currentBranchFlow: Flow<Branch> =
            flowOf(branchId)
                .filterNotNull()
                .flatMapConcat { flow { emit(branchRelationDao.getRelationFlowByBranchId(it).first()) } }
                .map { it.map() }

        val currentUserFlow: Flow<User> =
            flow { emit(userRelationDao.getRelationByUserId(cashierCashOperationsFeatureArgs.userId)) }
                .map { it.map() }

        return companyFlow
            .zip(currentBranchFlow) { t1, t2 -> Pair(t1, t2) }
            .zip(currentUserFlow) { t1, t2 -> Triple(t1.first, t1.second, t2) }
            .flatMapConcat { flow.invoke(it.second, it.first, it.third) }
    }
}