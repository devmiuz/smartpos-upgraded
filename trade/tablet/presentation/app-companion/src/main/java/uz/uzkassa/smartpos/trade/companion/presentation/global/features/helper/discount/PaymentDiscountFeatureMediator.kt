package uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.discount

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.Discount
import uz.uzkassa.smartpos.feature.helper.payment.discount.dependencies.PaymentDiscountFeatureArgs
import uz.uzkassa.smartpos.feature.helper.payment.discount.dependencies.PaymentDiscountFeatureCallback
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.PaymentDiscountFragment
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.discount.runner.PaymentDiscountFeatureRunner
import uz.uzkassa.smartpos.trade.companion.presentation.support.feature.FeatureMediator
import java.math.BigDecimal
import kotlin.properties.Delegates

class PaymentDiscountFeatureMediator(
) : FeatureMediator, PaymentDiscountFeatureArgs, PaymentDiscountFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    private var finishAction: ((Discount) -> Unit) by Delegates.notNull()
    override var amount: BigDecimal by Delegates.notNull()
    override var discountAmount: BigDecimal by Delegates.notNull()
    override var discountPercent: Double by Delegates.notNull()

    val featureRunner: PaymentDiscountFeatureRunner =
        FeatureRunnerImpl()

    override fun onBackFromPaymentDiscount() =
        backAction.invoke()

    override fun onFinish(discount: Discount) =
        finishAction.invoke(discount)

    private inner class FeatureRunnerImpl : PaymentDiscountFeatureRunner {
        override fun run(
            amount: BigDecimal,
            discountAmount: BigDecimal,
            discountPercent: Double,
            action: (SupportAppScreen) -> Unit
        ) {
            this@PaymentDiscountFeatureMediator.amount = amount
            this@PaymentDiscountFeatureMediator.discountAmount = discountAmount
            this@PaymentDiscountFeatureMediator.discountPercent = discountPercent
            action.invoke(Screens.PaymentDiscount)
        }

        override fun back(action: () -> Unit): PaymentDiscountFeatureRunner {
            backAction = action
            return this
        }

        override fun finish(action: (Discount) -> Unit): PaymentDiscountFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object PaymentDiscount : SupportAppScreen() {
            override fun getFragment(): Fragment =
                PaymentDiscountFragment.newInstance()
        }
    }
}