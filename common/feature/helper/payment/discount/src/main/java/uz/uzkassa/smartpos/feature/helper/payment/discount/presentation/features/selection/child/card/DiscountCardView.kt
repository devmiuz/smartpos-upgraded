package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.card

import moxy.MvpView

internal interface DiscountCardView : MvpView {

    fun onLoadingDiscountCard()

    fun onSuccessDiscountCard()

    fun onErrorDiscountCardCauseNotFound()

    fun onErrorDiscountCardCauseIsNotDiscount()

    fun onErrorDiscountCard(throwable: Throwable)
}