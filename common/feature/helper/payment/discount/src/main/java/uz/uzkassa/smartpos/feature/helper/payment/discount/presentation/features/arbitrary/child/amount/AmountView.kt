package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.amount

import moxy.MvpView
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountArbitrary

internal interface AmountView : MvpView {

    fun onDiscountArbitraryDefined(discountArbitrary: DiscountArbitrary)
}