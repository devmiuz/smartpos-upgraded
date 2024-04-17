package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.discount

import uz.uzkassa.smartpos.core.utils.math.multiply
import uz.uzkassa.smartpos.core.utils.math.times
import uz.uzkassa.smartpos.core.utils.primitives.roundToBigDecimal
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.DiscountType
import java.math.BigDecimal
import java.math.RoundingMode

internal data class SaleDiscount(
    val totalAmount: BigDecimal,
    val discountAmount: BigDecimal,
    val discountPercent: Double,
    val discountType: DiscountType
) {

    val getOrCalculateDiscountAmount: BigDecimal
        get() = when (discountType) {
            DiscountType.BY_SUM -> discountAmount
            DiscountType.BY_PERCENT -> when {
                discountPercent > 0.0 && discountPercent < 100.0 ->
                    ((totalAmount
                        .multiply(discountPercent))
                        .divide(BigDecimal(100), 2, RoundingMode.DOWN))
                discountPercent >= 100.0 -> totalAmount
                else -> BigDecimal.ZERO
            }
        }

    /**
     * это переменний нужно исползоват тогда когда скидка по суммам
     * */
    val getOrCalculateDiscountPercent: Double
        get() = when (discountType) {
            DiscountType.BY_PERCENT -> discountPercent
            DiscountType.BY_SUM -> {
                val percent = discountAmount
                    .multiply(100.toBigDecimal())
                    .divide(totalAmount, 2, RoundingMode.DOWN)
                    .toDouble()

                percent.coerceIn(0.0, 100.0)
            }
        }
}