package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.child.card

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.exception.CardIsNotDiscountException
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.exception.CardNotFoundException
import uz.uzkassa.smartpos.feature.helper.payment.discount.domain.DiscountCardInteractor
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.navigation.DiscountRouter
import javax.inject.Inject

internal class DiscountCardPresenter @Inject constructor(
    private val discountCardInteractor: DiscountCardInteractor,
    private val discountRouter: DiscountRouter
) : MvpPresenter<DiscountCardView>() {

    fun setCardNumber(value: String) =
        discountCardInteractor.setCardNumber(value)

    fun proceed() {
        discountCardInteractor
            .getDiscountCard()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingDiscountCard() }
            .onSuccess {
                viewState.onSuccessDiscountCard()
                discountRouter.exit()
            }
            .onFailure {
                when (it) {
                    is CardNotFoundException ->
                        viewState.onErrorDiscountCardCauseNotFound()
                    is CardIsNotDiscountException ->
                        viewState.onErrorDiscountCardCauseIsNotDiscount()
                    else -> viewState.onErrorDiscountCard(it)
                }
            }
    }
}