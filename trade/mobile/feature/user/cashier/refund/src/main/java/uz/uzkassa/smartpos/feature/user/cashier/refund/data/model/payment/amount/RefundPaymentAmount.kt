package uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.amount

import java.math.BigDecimal

internal data class RefundPaymentAmount(
    val payment: RefundReceiptPayment,
    val leftAmount: BigDecimal,
    val totalAmount: BigDecimal
)