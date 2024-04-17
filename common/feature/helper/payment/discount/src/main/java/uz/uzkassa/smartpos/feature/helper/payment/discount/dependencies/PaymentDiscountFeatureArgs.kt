package uz.uzkassa.smartpos.feature.helper.payment.discount.dependencies

import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountType
import java.math.BigDecimal

interface PaymentDiscountFeatureArgs {

    val amount: BigDecimal

    val discountAmount: BigDecimal

    val discountPercent: Double

    val discountType: DiscountType
}