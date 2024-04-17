package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.save

import android.util.Log
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.manager.printer.PrinterManager
import uz.uzkassa.smartpos.core.data.manager.printer.print.PrintData
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalReceiptResult
import uz.uzkassa.smartpos.core.data.source.fiscal.source.receipt.FiscalReceiptSource
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail.ReceiptDetailEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.payment.ReceiptPaymentEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao.ReceiptTemplateEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplate
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.mapper.receipt.mapToCashTransaction
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.print.CreateReceiptPrintData
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.ReceiptDraftRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.params.ReceiptDraftDeleteParams
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.save.params.ReceiptSaleSaveParams
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs
import javax.inject.Inject

internal class ReceiptSaleSaveRepositoryImpl @Inject constructor(
    private val branchRelationDao: BranchRelationDao,
    cashierSaleFeatureArgs: CashierSaleFeatureArgs,
    private val cashTransactionEntityDao: CashTransactionEntityDao,
    private val companyRelationDao: CompanyRelationDao,
    private val fiscalReceiptSource: FiscalReceiptSource,
    private val printerManager: PrinterManager,
    private val receiptDetailEntityDao: ReceiptDetailEntityDao,
    private val receiptDraftRepository: ReceiptDraftRepository,
    private val receiptEntityDao: ReceiptEntityDao,
    private val receiptPaymentEntityDao: ReceiptPaymentEntityDao,
    private val receiptTemplateEntityDao: ReceiptTemplateEntityDao,
    private val shiftReportEntityDao: ShiftReportEntityDao,
    private val userRelationDao: UserRelationDao
) : ReceiptSaleSaveRepository {
    private var lastFiscalReceiptResult: FiscalReceiptResult? = null
    private var lastReceipt: Receipt? = null
    private var lastReceiptPrintData: CreateReceiptPrintData? = null
    private var printCount: Int = 0
    private val branchId: Long = cashierSaleFeatureArgs.branchId
    private val userId: Long = cashierSaleFeatureArgs.userId

    @Suppress("LABEL_NAME_CLASH")
    @FlowPreview
    override fun createReceipt(params: ReceiptSaleSaveParams): Flow<Unit> {
        return getFiscalReceiptResult(params)
            .flatMapConcat { result ->
                getDetails { branch, company, receiptTemplate, user ->
                    getReceipt(params, company, result)
                        .flatMapConcat { receipt ->
                            getPrintData(branch, company, result, receipt, receiptTemplate, user)
                                .flatMapConcat {
                                    printerManager.print(it)
                                }
                                .onEach { printCount += 1 }
                        }
                        .flatMapConcat {
                            val receiptId: Long =
                                params.receiptDraftId ?: return@flatMapConcat flowOf(Unit)
                            return@flatMapConcat receiptDraftRepository
                                .deleteReceiptDraft(
                                    ReceiptDraftDeleteParams(
                                        receiptId,
                                        params.receiptUid!!
                                    )
                                )
                        }
                }
            }
            .map { }
    }

    @FlowPreview
    override fun printLastReceipt(): Flow<Unit> {
        return flowOf(lastReceiptPrintData)
            .onEach { it?.isDuplicate = printCount > 0 }
            .flatMapConcat {
                return@flatMapConcat if (it != null)
                    printerManager.print(it).onEach { printCount += 1 }
                else flowOf(Unit)
            }
    }

    override fun clearTempData(isCreditAdvance: Boolean): Flow<Unit> {
        return flowOf(Unit).onEach {
            lastFiscalReceiptResult = null
            lastReceipt = null
            if (!isCreditAdvance) lastReceiptPrintData = null
            printCount = 0
        }
    }

    @FlowPreview
    private fun getFiscalReceiptResult(params: ReceiptSaleSaveParams): Flow<FiscalReceiptResult?> {
        return flowOf(lastFiscalReceiptResult)
            .flatMapConcat { it ->
                return@flatMapConcat if (it != null) flowOf(it)
                else fiscalReceiptSource.createReceipt(params.getFiscalRequest(userId))
                    .onEach {
                        lastFiscalReceiptResult = it
                    }
                    .onEach {
                        Log.e("RECEIPT_RESULT", it?.toString() ?: "null")
                    }
                    .onEach {
                        if (it != null) lastReceipt = null
                    }
            }
    }

    @FlowPreview
    private fun getReceipt(
        params: ReceiptSaleSaveParams,
        company: Company,
        fiscalReceiptResult: FiscalReceiptResult?
    ): Flow<Receipt> {
        return flowOf(lastReceipt)
            .flatMapConcat { it ->
                return@flatMapConcat if (it != null) flowOf(it)
                else {
                    val element: Receipt = params.asReceipt(
                        userId = userId,
                        branchId = branchId,
                        companyId = company.id,
                        fiscalReceiptResult = fiscalReceiptResult
                    )

                    return@flatMapConcat flowOf(element)
                        .onEach {
                            receiptEntityDao.save(it)
                        }
                        .flatMapConcat { receipt ->
                            shiftReportEntityDao
                                .getOpenShiftReportEntity()
                                .onEach {
                                    if (it == null) return@onEach
                                    if (receipt.status == ReceiptStatus.CREDIT || receipt.status == ReceiptStatus.ADVANCE) {
                                        return@onEach
                                    }
                                    cashTransactionEntityDao.upsert(
                                        cashTransaction = receipt.mapToCashTransaction(),
                                        shiftId = it.id,
                                        shiftNumber = it.fiscalShiftNumber.toLong()
                                    )
                                }
                                .map { receipt }
                        }
                        .onEach { lastReceipt = it }
                }
            }
    }

    @FlowPreview
    private fun getPrintData(
        branch: Branch, company: Company, result: FiscalReceiptResult?,
        receipt: Receipt, template: ReceiptTemplate?, user: User
    ): Flow<PrintData> {
        return flowOf(lastReceiptPrintData)
            .flatMapConcat { it ->
                if (receipt.status == ReceiptStatus.CREDIT || receipt.status == ReceiptStatus.ADVANCE) {
                    val printData = CreateReceiptPrintData(
                        branch = branch,
                        company = company,
                        fiscalReceiptResult = result,
                        receipt = receipt,
                        receiptTemplate = template,
                        user = user
                    )

                    return@flatMapConcat flowOf(printData)
                        .onEach {
                            lastReceiptPrintData = it
                        }
                } else {
                    return@flatMapConcat if (it != null) flowOf(it)
                    else {
                        val printData = CreateReceiptPrintData(
                            branch = branch,
                            company = company,
                            fiscalReceiptResult = result,
                            receipt = receipt,
                            receiptTemplate = template,
                            user = user
                        )

                        return@flatMapConcat flowOf(printData)
                            .onEach {
                                lastReceiptPrintData = it
                            }
                    }
                }
            }
    }

    @FlowPreview
    private fun <T> getDetails(flow: (Branch, Company, ReceiptTemplate?, User) -> Flow<T>): Flow<T> {
        val companyFlow: Flow<Company> = flow { emit(companyRelationDao.getRelation().map()) }

        val currentBranchFlow: Flow<Branch> =
            flowOf(branchId)
                .filterNotNull()
                .flatMapConcat {
                    flow {
                        emit(
                            branchRelationDao.getRelationFlowByBranchId(it).first()
                        )
                    }
                }
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