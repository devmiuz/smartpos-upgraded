package uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.amount

import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.AmountType
import java.math.BigDecimal

internal data class RefundReceiptPayment(
    val allowedAmount: BigDecimal,
    val amount: BigDecimal,
    val type: AmountType
)