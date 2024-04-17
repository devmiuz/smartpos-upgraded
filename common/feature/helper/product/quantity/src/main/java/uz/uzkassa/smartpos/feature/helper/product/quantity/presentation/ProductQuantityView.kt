package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

internal interface ProductQuantityView : MvpView {

    fun onProductNameDefined(name: String)

    fun onUnitChanged(isCountable: Boolean)

    @OneExecution
    fun onErrorQuantityCauseNotDefined(throwable: Throwable)

    @OneExecution
    fun onDismissView()
}