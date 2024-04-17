package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.helper.payment.discount.domain.DiscountArbitraryInteractor
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.navigation.DiscountRouter
import javax.inject.Inject

internal class ArbitraryPresenter @Inject constructor(
    private val discountRouter: DiscountRouter,
    private val discountArbitraryInteractor: DiscountArbitraryInteractor
) : MvpPresenter<ArbitraryView>() {

    val isDiscountWithPercent: Boolean
        get() = discountArbitraryInteractor.isDiscountWithPercent

    fun getDiscountArbitraryResult() {
        discountArbitraryInteractor
            .getDiscountArbitraryResult()
            .launchCatchingIn(presenterScope)
    }

    fun backToRoot() =
        discountRouter.exit()
}