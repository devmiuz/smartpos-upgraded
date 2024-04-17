package uz.uzkassa.smartpos.trade.presentation.global.features.helper.discount.runner

import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.Discount
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountType
import java.math.BigDecimal

interface PaymentDiscountFeatureRunner {

    fun run(
        amount: BigDecimal,
        discountAmount: BigDecimal,
        discountPercent: Double,
        discountType: DiscountType,
        action: (SupportAppScreen) -> Unit
    )

    fun back(action: () -> Unit): PaymentDiscountFeatureRunner

    fun finish(action: (Discount) -> Unit): PaymentDiscountFeatureRunner
}