package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.cart

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.cart.RefundCart

internal interface RefundCartView : MvpView {

    fun onRefundReceiptDefined(refundCart: RefundCart)

    fun onToggleRefundProducts(isAllProductsSelected: Boolean)

    fun onShowExitAlert()

    fun onDismissExitAlert()

    fun onShowRequestMarkingAlert(product: RefundCart.Product)

    fun onDismissRequestMarkingAlert()

    @OneExecution
    fun onErrorAvailableCash(throwable: Throwable)
}