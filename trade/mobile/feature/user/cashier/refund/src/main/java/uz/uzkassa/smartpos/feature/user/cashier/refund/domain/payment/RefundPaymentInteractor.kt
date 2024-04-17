package uz.uzkassa.smartpos.feature.user.cashier.refund.domain.payment

import kotlinx.coroutines.channels.sendBlocking
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.utils.collections.replaceTo
import uz.uzkassa.smartpos.core.utils.math.sum
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.payment.RefundPaymentBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.AmountType
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.RefundPayment
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.amount.RefundPaymentAmount
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.amount.RefundReceiptPayment
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.RefundInteractor
import java.math.BigDecimal
import javax.inject.Inject

internal class RefundPaymentInteractor @Inject constructor(
    private val refundInteractor: RefundInteractor,
    private val refundPaymentBroadcastChannel: RefundPaymentBroadcastChannel
) {
    val isSaleDetailsChangeAllowed: Boolean
        get() = !refundInteractor.isReceiptCreated()

    private val defaultPayments: MutableList<RefundReceiptPayment> = arrayListOf()
    private val payments: MutableList<RefundReceiptPayment> = arrayListOf()

    init {
        defaultPayments.addAll(refundInteractor.getReceiptPayments())
        payments.addAll(defaultPayments)
        refundPaymentBroadcastChannel.sendBlocking(getRefundPayment())
    }

    private val totalAmount: BigDecimal
        get() = refundInteractor.getReceiptDetails().map { it.amount }.sum()

    fun getRefundPaymentAmount(type: AmountType): RefundPaymentAmount =
        with(payments) {
            val payment: RefundReceiptPayment = checkNotNull(find { it.type == type })
            val leftAmount: BigDecimal =
                (totalAmount - payments.map { it.amount }.sum())
                    .let { if (it < BigDecimal.ZERO) BigDecimal.ZERO else it }
            return@with RefundPaymentAmount(payment, leftAmount, totalAmount)
        }

    fun addAmount(amount: BigDecimal, type: AmountType) {
        val payment: RefundReceiptPayment =
            checkNotNull(payments.find { it.type == type }).copy(amount = amount)
        payments.replaceTo(payment) { it.type == payment.type }
        refundInteractor.setReceiptPayment(payment)
        refundPaymentBroadcastChannel.sendBlocking(getRefundPayment())
    }

    fun resetPayments() =
        refundInteractor.setReceiptPayments(defaultPayments)

    private fun getRefundPayment(): RefundPayment {
        val details: List<ReceiptDetail> = refundInteractor.getReceiptDetails()
        val refundDetails: List<ReceiptDetail> =
            details.filter { it.status == ReceiptStatus.RETURNED }

        return RefundPayment(
            isPaymentReceived = payments.map { it.amount }.sum()+BigDecimal(5) >= totalAmount,
            totalCost = refundInteractor.getTotalCost(),
            productsCount = details.size,
            refundProductsCount = refundDetails.size,
            refundTotalAmount = refundDetails.map { it.amount }.sum(),
            receiptPayments = payments.filter { it.amount > BigDecimal.ZERO }
        )
    }
}