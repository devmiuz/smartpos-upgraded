package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.arbitrary

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

internal interface ArbitraryAdmissionView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onHideSoftKeyboard()
}