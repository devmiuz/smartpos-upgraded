package uz.uzkassa.smartpos.feature.helper.payment.amount.presentation

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.Amount
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.AmountType
import java.math.BigDecimal

internal interface PaymentAmountView : MvpView {

    fun onAmountTypeSuccess(amountType: AmountType)

    fun onAmountChanged(amount: Amount)

    fun onAmountValueAdded(value: BigDecimal)

    fun onAmountValueChanged(value: BigDecimal)

    fun onErrorCauseAmountNotDefined()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onDismissView()
}