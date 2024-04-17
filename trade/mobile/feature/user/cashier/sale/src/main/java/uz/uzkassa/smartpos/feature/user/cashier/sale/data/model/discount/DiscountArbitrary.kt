package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount

import java.math.BigDecimal

data class DiscountArbitrary(
    val actualAmount: BigDecimal,
    val amount: BigDecimal,
    val discountAmount: BigDecimal,
    val discountPercent: Double,
    val discountType: DiscountType
)