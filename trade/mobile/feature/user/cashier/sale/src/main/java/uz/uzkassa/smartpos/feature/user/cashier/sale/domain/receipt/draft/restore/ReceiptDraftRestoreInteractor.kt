package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.receipt.draft.restore

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.draft.ReceiptDraftProductsBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.held.ReceiptHeldBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.mapper.cart.mapToItemType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.DiscountType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart.ItemType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.ReceiptDraftRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.SaleInteractor
import java.math.BigDecimal
import javax.inject.Inject

internal class ReceiptDraftRestoreInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val receiptDraftProductsBroadcastChannel: ReceiptDraftProductsBroadcastChannel,
    private val receiptDraftRepository: ReceiptDraftRepository,
    private val receiptHeldBroadcastChannel: ReceiptHeldBroadcastChannel,
    private val productRepository: ProductRepository,
    private val saleInteractor: SaleInteractor
) {

    val hasDataForRestore: Boolean
        get() = saleInteractor.getReceiptDetails().isNotEmpty()

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun restoreReceiptDraftById(id: Long): Flow<Result<Unit>> {
        return receiptDraftRepository.getReceiptDraftById(id)
            .onStart { saleInteractor.clear() }
            .onEach {
                val receipt: Receipt = it.receipt
                with(saleInteractor) {
                    setReceiptDraftId(id)
                    setReceiptUid(receipt.uid)
                    // TODO: 24/08/21 fix set discount values
                    setSaleDiscount(BigDecimal.ZERO, receipt.discountPercent, DiscountType.BY_PERCENT)
                    setReceiptPayments(receipt.receiptPayments)
                }
            }
            .flatMapConcat { it ->
                generateProduct(it.isRemote, it.receipt.receiptDetails)
                    .onEach { saleInteractor.setItemTypes(it) }
            }
            .onEach { receiptHeldBroadcastChannel.send(Unit) }
            .onEach { receiptDraftProductsBroadcastChannel.send(it) }
            .map { Unit }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    @FlowPreview
    private suspend fun generateProduct(
        isRemote: Boolean,
        details: List<ReceiptDetail>
    ): Flow<List<ItemType.Product>> {
        return flowOf(details)
            .flatMapConcat { it ->
                val products: List<ItemType.Product?> =
                    it
                        .map { detail ->
                            if (isRemote || detail.productId == null)
                                return@map detail.mapToItemType(null) as ItemType.Product
                            val productId: Long = requireNotNull(detail.productId)
                            val product = runCatching {
                                productRepository.getProductByProductId(productId).first()
                            }.getOrNull()
                            return@map detail.mapToItemType(product) as ItemType.Product
                        }
                return@flatMapConcat flowOf(products.filterNotNull())
            }
    }
}