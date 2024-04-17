package uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.discount.runner

import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.Discount
import java.math.BigDecimal

interface PaymentDiscountFeatureRunner {

    fun run(
        amount: BigDecimal,
        discountAmount: BigDecimal,
        discountPercent: Double,
        action: (SupportAppScreen) -> Unit
    )

    fun back(action: () -> Unit): PaymentDiscountFeatureRunner

    fun finish(action: (Discount) -> Unit): PaymentDiscountFeatureRunner
}