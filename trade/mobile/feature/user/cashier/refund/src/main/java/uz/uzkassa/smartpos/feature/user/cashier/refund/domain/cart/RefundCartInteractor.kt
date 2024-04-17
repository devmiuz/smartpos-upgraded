package uz.uzkassa.smartpos.feature.user.cashier.refund.domain.cart

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.collections.replace
import uz.uzkassa.smartpos.core.utils.collections.replaceTo
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.product_marking.data.model.ProductMarkingResult
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.cart.RefundCartBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.mapper.cart.mapToProduct
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.cart.RefundCart
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.quantity.ProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.cash.CashOperationsDetailsRepository
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.RefundInteractor
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

internal class RefundCartInteractor @Inject constructor(
    private val refundCartBroadcastChannel: RefundCartBroadcastChannel,
    private val refundInteractor: RefundInteractor,
    private val coroutineContextManager: CoroutineContextManager,
    private val cashOperationsDetailsRepository: CashOperationsDetailsRepository
) {
    private val refundCartDetails: MutableList<RefundCart.Product> = arrayListOf()
    internal var pendingUpdatedQuantity: ProductQuantity? = null

    init {
        refundCartDetails.addAll(refundInteractor.getRefundProduct())
        refundCartBroadcastChannel.setUid(refundInteractor.getReceiptUid())
        refundCartBroadcastChannel.sendBlocking(refundCartDetails)
    }

    fun uncheckProduct(result: ProductMarkingResult) {
        val element: RefundCart.Product = refundCartDetails.find {
            val isSameProduct: Boolean = it.productId == result.productId
            val isSameUnit: Boolean = it.detailUnit?.id == result.lastUnitId
            return@find isSameProduct && isSameUnit
        } ?: return

        setToRefund(element, false)
    }

    fun setToRefund(product: RefundCart.Product, enable: Boolean) {
        val updatedProduct: RefundCart.Product = updateProduct(product, enable)
        refundCartDetails.replaceTo(updatedProduct) {
            val isSameProduct: Boolean = it.productId == product.productId
            val isSameUnit: Boolean = it.unit?.id == product.unit?.id
            return@replaceTo isSameProduct && isSameUnit
        }
        setReceiptDetail(updatedProduct)
    }

    fun toggleRefundProducts(enable: Boolean) {
        val details: List<RefundCart.Product> = refundCartDetails.map {
            updateProduct(
                it,
                if (it.totalMarkings.isNullOrEmpty()) enable else if (it.forRefund) enable else false
            )
        }
        refundCartDetails.clear()
        refundCartDetails.addAll(details)
        details.forEach {
            refundInteractor.setReceiptDetail(it)
        }
        refundCartBroadcastChannel.sendBlocking(details)
    }

    fun updateQuantity(result: ProductQuantity) {
        val element: RefundCart.Product = refundCartDetails.find {
            val isSameProduct: Boolean = it.productId == result.productId
            val isSameUnit: Boolean = it.detailUnit?.id == result.lastUnitId
            return@find isSameProduct && isSameUnit
        } ?: return

        val updatedDetail: RefundCart.Product = result.mapToProduct(element)
        refundCartDetails.replace(element, updatedDetail)
        setReceiptDetail(updatedDetail)
    }

    fun updateMarkings(result: ProductMarkingResult) {
        val element: RefundCart.Product = refundCartDetails.find {
            val isSameProduct: Boolean = it.productId == result.productId
            val isSameUnit: Boolean = it.detailUnit?.id == result.lastUnitId
            return@find isSameProduct && isSameUnit
        } ?: return

        val updatedQuantityProduct: RefundCart.Product? =
            pendingUpdatedQuantity?.mapToProduct(element)

        if (element.markedMarkings != null) {
            val markedMarkings = element.markedMarkings.toMutableList()
            val newMarkings = result.markings.toMutableList()

            newMarkings.forEach { mark ->
                if (markedMarkings.contains(mark))
                    markedMarkings.remove(mark) else markedMarkings.add(mark)
            }

            var updatedDetail: RefundCart.Product = updateProduct(element, true)
            updatedDetail = if (pendingUpdatedQuantity == null) updatedDetail.copy(
                markedMarkings = markedMarkings.toTypedArray(),
                forRefund = true
            ) else updatedQuantityProduct!!.copy(
                markedMarkings = markedMarkings.toTypedArray(),
                forRefund = true
            )
            refundCartDetails.replace(element, updatedDetail)
            setReceiptDetail(
                updatedDetail
            )
        }
    }

    private fun updateProduct(product: RefundCart.Product, enable: Boolean): RefundCart.Product {
        val quantity: Double = if (enable) product.detailQuantity else 0.0
        val amount: BigDecimal = if (enable) product.detailAmount else BigDecimal.ZERO
        val actualAmount: BigDecimal =
            if (amount > BigDecimal.ZERO && amount >= product.discountAmount) amount - product.discountAmount
            else amount

        val status: ReceiptStatus = if (enable) ReceiptStatus.RETURNED else ReceiptStatus.PAID
        val unit: Unit? = if (enable) product.unit else product.detailUnit

        return product.copy(
            amount = actualAmount,
            forRefund = enable,
            quantity = quantity,
            status = status,
            unit = unit
        )
    }

    private fun setReceiptDetail(product: RefundCart.Product) {
        refundInteractor.setReceiptDetail(product)
        refundCartBroadcastChannel.sendBlocking(refundCartDetails)
    }

    fun setAvailableCash(shiftAvailableCash: BigDecimal) {
        refundCartBroadcastChannel.setAvailableCash(shiftAvailableCash)
    }

    fun getAvailableCash(): Flow<Result<BigDecimal>> {
        return cashOperationsDetailsRepository.getAvailableCash()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}
