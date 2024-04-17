package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.amount

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountArbitrary
import uz.uzkassa.smartpos.feature.helper.payment.discount.domain.DiscountArbitraryInteractor
import java.math.BigDecimal
import javax.inject.Inject

internal class AmountPresenter @Inject constructor(
    private val discountArbitraryInteractor: DiscountArbitraryInteractor,
    private val discountLazyFlow: Lazy<Flow<DiscountArbitrary>>
) : MvpPresenter<AmountView>() {

    override fun onFirstViewAttach() {
        getProvidedDiscountArbitrary()
    }

    fun addDiscountAmount(value: String) =
        discountArbitraryInteractor.addAmount(BigDecimal(value))

    fun setDiscountAmount(value: String) =
        discountArbitraryInteractor.setAmount(BigDecimal(value))

    private fun getProvidedDiscountArbitrary() {
        discountLazyFlow.get()
            .onEach { viewState.onDiscountArbitraryDefined(it) }
            .launchIn(presenterScope)
    }
}