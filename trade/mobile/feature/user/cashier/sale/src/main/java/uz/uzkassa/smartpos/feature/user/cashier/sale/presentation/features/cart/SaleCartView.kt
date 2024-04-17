package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart

@AddToEndSingle
internal interface SaleCartView : MvpView {

    fun onCartDefined(saleCart: SaleCart)

    @OneExecution
    fun onShowFreePriceInput()

    @OneExecution
    fun onShowChangeNotAllowedAlert(isDelete: Boolean)

    fun onDismissChangeNotAllowedAlert()

    @OneExecution
    fun onErrorProductBarcodeScan(throwable: Throwable)

    fun onCleared()

    @OneExecution
    fun onShowFinishShiftAlert()

    fun onDismissFinishShiftAlert()
}