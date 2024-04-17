package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.receipt.draft.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarking
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraft
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.ReceiptDraftRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.params.ReceiptDraftDeleteParams
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.marking.ProductMarkingInteractor
import javax.inject.Inject

internal class ReceiptDraftListInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val receiptDraftRepository: ReceiptDraftRepository,
    private val productMarkingInteractor: ProductMarkingInteractor
) {

    fun getReceiptDrafts(search: String?): Flow<Result<List<ReceiptDraft>>> =
        receiptDraftRepository
            .getReceiptDrafts(search)
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)

    fun deleteReceiptDraftById(receiptDraft: ReceiptDraft): Flow<Result<Unit>> =
        receiptDraftRepository
            .deleteReceiptDraft(ReceiptDraftDeleteParams(receiptDraft.id, receiptDraft.receipt.uid))
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)


    fun deleteProductMarkings(receipt: Receipt): Flow<Result<Unit>> {
        val productMarkings: MutableList<ProductMarking> = ArrayList()
        receipt.receiptDetails
            .filter { !it.marks.isNullOrEmpty() }
            .forEach { detail ->
                detail.marks!!.forEach {
                    productMarkings.add(ProductMarking(productId = detail.productId, marking = it))
                }
            }
        return productMarkingInteractor.deleteProductMarkings(productMarkings)
    }
}