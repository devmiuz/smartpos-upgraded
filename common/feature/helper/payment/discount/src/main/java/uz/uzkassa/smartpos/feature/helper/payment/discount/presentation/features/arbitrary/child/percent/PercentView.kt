package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.percent

import moxy.MvpView
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountArbitrary

internal interface PercentView : MvpView {

    fun onSaleDiscountArbitraryDefined(discountArbitrary: DiscountArbitrary)
}