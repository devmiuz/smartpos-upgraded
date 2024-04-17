package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.amount

import moxy.MvpView
import java.math.BigDecimal

internal interface AmountView : MvpView {

    fun onAmountDefined(amount: BigDecimal)

    fun onAmountChanged(amount: BigDecimal)
}