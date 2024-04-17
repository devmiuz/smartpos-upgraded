package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.freeprice

import moxy.MvpPresenter
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.freeprice.FreePriceInteractor
import javax.inject.Inject

@Suppress("unused", "UNUSED_PARAMETER")
internal class FreePricePresenter @Inject constructor(
    private val freePriceInteractor: FreePriceInteractor
) : MvpPresenter<FreePriceView>() {

    override fun onFirstViewAttach() {
    }

    fun addAmount(value: String) {
    }

    fun setAmount(value: String) {
    }

    fun dismiss() =
        viewState.onDismissView()

    fun getShoppingBagFreePrice() {
    }
}