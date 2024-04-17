package uz.uzkassa.smartpos.feature.user.cashier.refund.domain

import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.ReceiptRefundInfo
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.utils.collections.removeBy
import uz.uzkassa.smartpos.core.utils.collections.replaceTo
import uz.uzkassa.smartpos.core.utils.math.sum
import uz.uzkassa.smartpos.core.utils.util.formatOFDDateString
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.mapper.cart.mapToProduct
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.mapper.receipt.mapToReceiptDetail
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.AmountType
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.cart.RefundCart
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.amount.RefundReceiptPayment
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.*
import kotlin.properties.Delegates

@Suppress("MemberVisibilityCanBePrivate")
internal class RefundInteractor {
    private var isReceiptCreated: Boolean = false
    private val receiptDetails: MutableList<RefundCart.Product> = arrayListOf()
    private val receiptPayments: MutableList<RefundReceiptPayment> = arrayListOf()

    private var uid: String by Delegates.notNull()
    private var totalAmount: BigDecimal by Delegates.notNull()
    private var receiptRefundInfo: ReceiptRefundInfo by Delegates.notNull()

    private var uniqueId: String? = Date().time.toString()
    private var serialNumber: String? = null

    fun setReceiptForRefund(receipt: Receipt) {
        uid = receipt.uid
        receiptRefundInfo = ReceiptRefundInfo(
            terminalID = receipt.terminalId,
            receiptSeq = receipt.receiptSeq,
            fiscalReceiptCreatedDate = receipt.fiscalReceiptCreatedDate?.formatOFDDateString(),
            fiscalSign = receipt.fiscalSign
        )
        totalAmount = receipt.totalCost
        receiptDetails.apply {
            clear()
            addAll(
                receipt.receiptDetails.map {
                    it.mapToProduct()
                }
            )
        }
        receiptPayments.apply {
            clear()
            add(RefundReceiptPayment(receipt.totalCard, ZERO, AmountType.CARD))
            add(RefundReceiptPayment(receipt.totalCash, ZERO, AmountType.CASH))
        }
        uniqueId = Date().time.toString()
    }

    fun getReceiptUid(): String = uid

    fun getRefundInfo(): ReceiptRefundInfo = receiptRefundInfo

    fun getRefundProduct(): List<RefundCart.Product> = receiptDetails

    fun getReceiptDetails(): List<ReceiptDetail> =
        receiptDetails.map {
            it.mapToReceiptDetail()
        }

    fun getReceiptPayments(): List<RefundReceiptPayment> =
        receiptPayments

    fun getTotalCost(): BigDecimal =
        totalAmount - getTotalDiscount()

    fun getTotalDiscount(): BigDecimal =
        receiptDetails.map { it.discountAmount }.sum()

    fun setReceiptCreated(value: Boolean) {
        isReceiptCreated = value
    }

    fun isReceiptCreated(): Boolean =
        isReceiptCreated

    fun deleteRefundReceiptDetail(product: RefundCart.Product) {
        receiptDetails.removeBy {
            val isSameProduct: Boolean = it.productId == product.productId
            val isSameUnit: Boolean = it.detailUnit?.id == product.detailUnit?.id
            return@removeBy isSameProduct && isSameUnit
        }
    }

    fun setReceiptDetail(product: RefundCart.Product) {
        deleteRefundReceiptDetail(product)
        val updatedProduct = product.copy(
            amount = product.amount,
            quantity = product.quantity,
            status = if (product.quantity > 0.0) ReceiptStatus.RETURNED else ReceiptStatus.PAID,
            unit = product.unit,
            markedMarkings = product.markedMarkings
        )
        receiptDetails.add(updatedProduct)
    }

    fun setReceiptPayment(payment: RefundReceiptPayment) {
        receiptPayments.replaceTo(payment) { it.type == payment.type }
    }

    fun setReceiptPayments(payments: List<RefundReceiptPayment>) {
        receiptPayments.apply { clear(); addAll(payments) }
    }

    fun getUniqueId(serialNumber: String): String {
        this.serialNumber = if (serialNumber.length >= 6) serialNumber.substring(serialNumber.length - 6) else serialNumber
        return this.serialNumber + uniqueId
    }
}