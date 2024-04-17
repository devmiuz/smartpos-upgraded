package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.arbitrary

import moxy.MvpPresenter
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.navigation.DiscountRouter
import javax.inject.Inject

internal class ArbitraryAdmissionPresenter @Inject constructor(
    private val discountRouter: DiscountRouter
) : MvpPresenter<ArbitraryAdmissionView>() {

    @Suppress("UNUSED_PARAMETER")
    fun setAdminPassword(value: String) {

    }

    fun proceed() {
        viewState.onHideSoftKeyboard()
        discountRouter.openDiscountArbitraryScreen()
    }
}