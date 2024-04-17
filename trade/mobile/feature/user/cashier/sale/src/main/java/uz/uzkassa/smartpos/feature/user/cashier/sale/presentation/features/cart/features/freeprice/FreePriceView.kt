package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.freeprice

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.math.BigDecimal

internal interface FreePriceView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onDismissView()

    fun onLoadingFreePriceAmount()

    fun onSuccessFreePriceAmount()

    fun onAmountAdded(bigDecimal: BigDecimal)

    fun onAmountChanged(bigDecimal: BigDecimal)

    fun onLoadingCompleteFreePriceAmount()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onSuccessCompleteFreePriceAmount()

    fun onErrorCompleteFreePriceAmountCauseNotInputted()
}