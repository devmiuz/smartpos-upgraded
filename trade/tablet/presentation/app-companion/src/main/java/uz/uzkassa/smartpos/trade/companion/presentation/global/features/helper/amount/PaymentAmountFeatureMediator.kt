package uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.amount

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.Amount
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.AmountType
import uz.uzkassa.smartpos.feature.helper.payment.amount.dependencies.PaymentAmountFeatureArgs
import uz.uzkassa.smartpos.feature.helper.payment.amount.dependencies.PaymentAmountFeatureCallback
import uz.uzkassa.smartpos.feature.helper.payment.amount.presentation.PaymentAmountFragment
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.amount.runner.PaymentAmountFeatureRunner
import uz.uzkassa.smartpos.trade.companion.presentation.support.feature.FeatureMediator
import java.math.BigDecimal
import kotlin.properties.Delegates

class PaymentAmountFeatureMediator(
) : FeatureMediator, PaymentAmountFeatureArgs, PaymentAmountFeatureCallback {
    private var finishAction: ((Amount) -> Unit) by Delegates.notNull()
    override var amount: BigDecimal by Delegates.notNull()
    override var allowedAmount: BigDecimal? = null
    override var amountType: AmountType by Delegates.notNull()
    override var branchId: Long by Delegates.notNull()
    override var leftAmount: BigDecimal by Delegates.notNull()
    override var totalAmount: BigDecimal by Delegates.notNull()

    val featureRunner: PaymentAmountFeatureRunner =
        FeatureRunnerImpl()

    override fun onFinish(amount: Amount) =
        finishAction.invoke(amount)

    private inner class FeatureRunnerImpl : PaymentAmountFeatureRunner {

        override fun run(
            amount: BigDecimal,
            allowedAmount: BigDecimal?,
            amountType: AmountType,
            branchId: Long,
            leftAmount: BigDecimal,
            totalAmount: BigDecimal,
            action: (Screen) -> Unit
        ) {
            this@PaymentAmountFeatureMediator.amount = amount
            this@PaymentAmountFeatureMediator.allowedAmount = allowedAmount
            this@PaymentAmountFeatureMediator.amountType = amountType
            this@PaymentAmountFeatureMediator.branchId = branchId
            this@PaymentAmountFeatureMediator.leftAmount = leftAmount
            this@PaymentAmountFeatureMediator.totalAmount = totalAmount
            action.invoke(Screens.PaymentAmount)
        }

        override fun finish(action: (Amount) -> Unit): PaymentAmountFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object PaymentAmount : SupportAppScreen() {
            override fun getFragment(): Fragment =
                PaymentAmountFragment.newInstance()
        }
    }
}