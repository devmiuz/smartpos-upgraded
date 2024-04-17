package uz.uzkassa.smartpos.feature.helper.payment.discount.data.model

import java.math.BigDecimal

data class DiscountArbitrary(
    val actualAmount: BigDecimal,
    val amount: BigDecimal,
    val discountAmount: BigDecimal,
    val discountPercent: Double,
    val discountType: DiscountType
)