package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user.session

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.manager.printer.PrinterManager
import uz.uzkassa.smartpos.core.data.manager.printer.print.PrintData
import uz.uzkassa.smartpos.core.data.source.fiscal.model.shift.FiscalCloseShiftResult
import uz.uzkassa.smartpos.core.data.source.fiscal.source.shift.FiscalShiftSource
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao.ReceiptTemplateEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplate
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.print.ZReportPrintData
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.shift.ShiftReportRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs
import javax.inject.Inject

internal class UserSessionRepositoryImpl @Inject constructor(
    private val branchRelationDao: BranchRelationDao,
    cashierSaleFeatureArgs: CashierSaleFeatureArgs,
    private val companyRelationDao: CompanyRelationDao,
    private val fiscalShiftSource: FiscalShiftSource,
    private val printerManager: PrinterManager,
    private val receiptTemplateEntityDao: ReceiptTemplateEntityDao,
    private val shiftReportRepository: ShiftReportRepository,
    private val userAuthRestService: UserAuthRestService,
    private val userRelationDao: UserRelationDao,
    private val userEntityDao: UserEntityDao
) : UserSessionRepository {
    private val branchId: Long = cashierSaleFeatureArgs.branchId
    private val userId: Long = cashierSaleFeatureArgs.userId
    private var lastReceiptPrintData: PrintData? = null

    @FlowPreview
    override fun pauseSession(): Flow<Unit> {
        return fiscalShiftSource.pauseShift()
            .flatMapConcat { shiftReportRepository.pauseShift() }
            .flatMapConcat { userAuthRestService.logout() }
    }

    @FlowPreview
    override fun logout(): Flow<Unit> {
        return shiftReportRepository.closeShift(userId)
            .flatMapConcat { it ->
                return@flatMapConcat if (it != null)
                    getDetailsForPrintData { branch, company, receiptTemplate, user ->
                        getPrintData(branch, company, it, receiptTemplate, user)
                            .flatMapConcat { printerManager.print(it) }
                    }
                else flowOf(Unit)
            }
            .flatMapConcat { userAuthRestService.logout() }

    }

    @FlowPreview
    private fun getPrintData(
        branch: Branch, company: Company,
        result: FiscalCloseShiftResult,
        template: ReceiptTemplate?,
        user: User
    ): Flow<PrintData> {
        return flowOf(lastReceiptPrintData)
            .flatMapConcat { it ->
                return@flatMapConcat if (it != null) flowOf(it)
                else flowOf(ZReportPrintData(branch, company, result, template, user))
                    .onEach { lastReceiptPrintData = it }
            }
    }

    @FlowPreview
    private fun <T> getDetailsForPrintData(
        flow: (Branch, Company, ReceiptTemplate?, User) -> Flow<T>
    ): Flow<T> {
        val companyFlow: Flow<Company> = flow { emit(companyRelationDao.getRelation().map()) }

        val currentBranchFlow: Flow<Branch> =
            flowOf(branchId)
                .filterNotNull()
                .flatMapConcat { flow { emit(branchRelationDao.getRelationFlowByBranchId(it).first()) } }
                .map { it.map() }

        val currentUserFlow: Flow<User> =
            flow { emit(userRelationDao.getRelationByUserId(userId)) }
                .map { it.map() }

        val receiptTemplateFlow: Flow<ReceiptTemplate?> =
            flow { emit(receiptTemplateEntityDao.getEntity()) }
                .map { it?.map() }

        return companyFlow
            .zip(currentBranchFlow) { t1, t2 -> Pair(t1, t2) }
            .zip(currentUserFlow) { t1, t2 -> Triple(t1.first, t1.second, t2) }
            .zip(receiptTemplateFlow) { t1, t2 -> Pair(t1, t2) }
            .flatMapConcat { flow(it.first.second, it.first.first, it.second, it.first.third) }
    }
}