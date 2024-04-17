package uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount

import java.math.BigDecimal

data class Amount(
    val amount: BigDecimal,
    val changeAmount: BigDecimal,
    val leftAmount: BigDecimal,
    val totalAmount: BigDecimal,
    val type: AmountType
)