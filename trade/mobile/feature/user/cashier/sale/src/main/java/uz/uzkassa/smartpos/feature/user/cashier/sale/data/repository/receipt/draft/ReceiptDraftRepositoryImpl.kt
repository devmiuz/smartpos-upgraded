package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.dao.ReceiptDraftEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.dao.ReceiptDraftRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraft
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraftEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.params.ReceiptDraftDeleteParams
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.params.ReceiptDraftSaveParams
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.save.params.ReceiptSaleSaveParams
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs
import java.util.*
import javax.inject.Inject

internal class ReceiptDraftRepositoryImpl @Inject constructor(
    cashierSaleFeatureArgs: CashierSaleFeatureArgs,
    private val companyRelationDao: CompanyRelationDao,
    private val receiptDraftEntityDao: ReceiptDraftEntityDao,
    private val receiptDraftRelationDao: ReceiptDraftRelationDao,
    private val receiptRestService: ReceiptRestService
) : ReceiptDraftRepository {
    private val branchId: Long = cashierSaleFeatureArgs.branchId
    private val userId: Long = cashierSaleFeatureArgs.userId
    private var lastReceipt: Receipt? = null

    @FlowPreview
    override fun createReceiptDraft(params: ReceiptDraftSaveParams): Flow<Unit> {
        return flow { emit(companyRelationDao.getRelation()) }
            .flatMapConcat { getReceipt(params.receiptSaveParams, it.map()) }
            .onEach {
                val entity = ReceiptDraftEntity(
                    receiptUid = it.uid,
                    name = params.name,
                    isRemote = false
                )
                receiptDraftEntityDao.save(params.receiptSaveParams.receiptDraftId, entity, it)
            }
            .map { }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @FlowPreview
    override fun deleteReceiptDraft(params: ReceiptDraftDeleteParams): Flow<Unit> {
        return flow { emit(receiptDraftEntityDao.getDraftByUid(params.uid)) }
            .flatMapConcat {
                if (it.isRemote) {
                    return@flatMapConcat receiptRestService.deleteReceipt(params.uid)
                        .onEach {
                            flow { emit(receiptDraftEntityDao.deleteById(params.id)) }
                        }.map { Unit }
                } else {
                    flow { emit(receiptDraftEntityDao.deleteById(params.id)) }.map { Unit }
                }
            }
    }

    @FlowPreview
    override fun getReceiptDraftById(id: Long): Flow<ReceiptDraft> {
        return receiptDraftRelationDao.getRelationById(id).map { it.map() }
    }

    @FlowPreview
    override fun getReceiptDrafts(search: String?): Flow<List<ReceiptDraft>> {
        return receiptRestService.getReceiptDrafts()
            .map { it ->
                receiptDraftEntityDao.deleteRemoteDraftReceipts()
                it.filter { it.receiptDetails.isNotEmpty() }
            }
            .onEach { it ->
                val entities: List<ReceiptDraftEntity> = it.map {
                    ReceiptDraftEntity(it.uid, "", true)
                }
                receiptDraftEntityDao.deleteSyncedDrafts(it.map { it.uid })
                receiptDraftEntityDao.save(entities, it)
            }
            .flatMapConcat {
                receiptDraftRelationDao.getRelations()
            }
            .map { it ->
                val result = if (search.isNullOrBlank()) {
                    it.map {
                        it.map()
                    }.asReversed()
                } else {
                    it.filter {
                        it.receiptEntity.customerName?.toLowerCase(Locale.ROOT)
                            ?.contains(search.toLowerCase(Locale.ROOT), true) ?: false ||
                                it.receiptEntity.customerContact?.toLowerCase(Locale.ROOT)
                                    ?.contains(search.toLowerCase(Locale.ROOT), true) ?: false
                    }.map {
                        it.map()
                    }.asReversed()
                }
                result
            }
    }

    @FlowPreview
    private fun getReceipt(params: ReceiptSaleSaveParams, company: Company): Flow<Receipt> {
        return flowOf(lastReceipt)
            .flatMapConcat { it ->
                return@flatMapConcat if (it != null) flowOf(it)
                else flowOf(params.asReceipt(userId, branchId, company.id))
                    .onEach { lastReceipt = it }
            }
    }
}