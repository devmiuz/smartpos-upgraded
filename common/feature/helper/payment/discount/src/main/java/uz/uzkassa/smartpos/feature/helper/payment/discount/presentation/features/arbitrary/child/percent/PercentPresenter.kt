package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.percent

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountArbitrary
import uz.uzkassa.smartpos.feature.helper.payment.discount.domain.DiscountArbitraryInteractor
import javax.inject.Inject

internal class PercentPresenter @Inject constructor(
    private val discountArbitraryInteractor: DiscountArbitraryInteractor,
    private val discountLazyFlow: Lazy<Flow<DiscountArbitrary>>
) : MvpPresenter<PercentView>() {

    override fun onFirstViewAttach() {
        getProvidedDiscountAmount()
    }

    fun setDiscountPercent(value: String) =
        discountArbitraryInteractor.setPercent(value.toDouble())

    private fun getProvidedDiscountAmount() {
        discountLazyFlow.get()
            .onEach { viewState.onSaleDiscountArbitraryDefined(it) }
            .launchIn(presenterScope)
    }
}