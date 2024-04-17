package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.details

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.details.CashOperationsDetails
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.dependencies.CashierCashOperationsFeatureArgs
import java.math.BigDecimal
import javax.inject.Inject

internal class CashOperationsDetailsRepositoryImpl @Inject constructor(
    private val branchRelationDao: BranchRelationDao,
    private val companyRelationDao: CompanyRelationDao,
    private val cashTransactionEntityDao: CashTransactionEntityDao,
    private val cashOperationsFeatureArgs: CashierCashOperationsFeatureArgs,
    private val shiftRepoEntityDao: ShiftReportEntityDao
) : CashOperationsDetailsRepository {

    @FlowPreview
    override fun getCashOperationsDetails(): Flow<CashOperationsDetails> {
        return shiftRepoEntityDao.getOpenShiftReportEntity()
            .filterNotNull()
            .map { it.id }
            .flatMapConcat { shiftId ->
                cashTransactionEntityDao.getEntitiesByShiftId(shiftId)
                    .map { list -> list.map { it.map() } }
                    .flatMapConcat { transactions ->
                        val branch: Branch = branchRelationDao
                            .getRelationFlowByBranchId(cashOperationsFeatureArgs.branchId)
                            .first().map()

                        val company: Company = companyRelationDao.getRelation().map()

                        val totalAmount: BigDecimal = if (transactions.isEmpty()) {
                            cashTransactionEntityDao.getCashTotalAmount() ?: BigDecimal.ZERO
                        } else {
                            transactions.lastOrNull()?.totalAmount ?: BigDecimal.ZERO
                        }

                        val totalEncashment: BigDecimal? =
                            transactions.filter { it.operation == CashOperation.INCASSATION }
                                .sumByDouble { it.totalCash.toDouble() }.toBigDecimal()

                        val totalSale: BigDecimal? =
                            transactions.filter { it.operation == CashOperation.PAID }
                                .sumByDouble { it.totalCash.toDouble() }.toBigDecimal()

                        val totalRefund: BigDecimal? =
                            transactions.filter { it.operation == CashOperation.RETURNED }
                                .sumByDouble { it.totalCash.toDouble() }.toBigDecimal()


                        val totalCash: BigDecimal = transactions.firstOrNull()?.let {
                            when (it.operation) {
                                CashOperation.INCOME,
                                CashOperation.PAID,
//                                CashOperation.ADVANCE,
//                                CashOperation.CREDIT,
                                CashOperation.RETURN_FLOW -> it.totalAmount - it.totalCash

                                CashOperation.RETURNED,
                                CashOperation.FLOW,
                                CashOperation.INCASSATION,
                                CashOperation.WITHDRAW,
                                CashOperation.UNKNOWN -> it.totalAmount + it.totalCash
                            }
                        } ?: totalAmount

                        val totalAddedCash: BigDecimal =
                            transactions.filter { it.operation == CashOperation.INCOME }
                                .sumByDouble { it.totalCash.toDouble() }.toBigDecimal()

                        val totalReturnedAddedCash: BigDecimal =
                            transactions.filter { it.operation == CashOperation.WITHDRAW }
                                .sumByDouble { it.totalCash.toDouble() }.toBigDecimal()

                        val totalExpense: BigDecimal =
                            transactions.filter { it.operation == CashOperation.FLOW }
                                .sumByDouble { it.totalCash.toDouble() }.toBigDecimal()

                        val totalReturnedExpense: BigDecimal =
                            transactions.filter { it.operation == CashOperation.RETURN_FLOW }
                                .sumByDouble { it.totalCash.toDouble() }.toBigDecimal()

                        val cashOperationsDetails = CashOperationsDetails(
                            companyAddress = company.address,
                            companyBranchName = branch.name,
                            companyBusinessType = company.companyBusinessType.nameRu,
                            companyName = company.name,
                            totalEncashment = totalEncashment,
                            totalAmount = totalAmount,
                            totalCash = totalCash,
                            totalRefund = totalRefund,
                            totalRefundCash = totalRefund,
                            totalSale = totalSale,
                            totalSaleCash = totalSale,
                            totalAddedCash = totalAddedCash,
                            totalReturnedAddedCash = totalReturnedAddedCash,
                            totalExpense = totalExpense,
                            totalReturnedExpense = totalReturnedExpense
                        )

                        flowOf(cashOperationsDetails)
                    }
            }
    }
}