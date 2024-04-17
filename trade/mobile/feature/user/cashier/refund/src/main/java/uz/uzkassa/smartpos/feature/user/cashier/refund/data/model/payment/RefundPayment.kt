package uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment

import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.amount.RefundReceiptPayment
import java.math.BigDecimal

internal data class RefundPayment(
    val isPaymentReceived: Boolean,
    val productsCount: Int,
    val refundProductsCount: Int,
    val refundTotalAmount: BigDecimal,
    val totalCost: BigDecimal,
    val receiptPayments: List<RefundReceiptPayment>
)