package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.amount

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.model.ProductQuantity
import uz.uzkassa.smartpos.feature.helper.product.quantity.domain.ProductQuantityInteractor
import java.math.BigDecimal
import javax.inject.Inject

internal class AmountPresenter @Inject constructor(
    private val productQuantityInteractor: ProductQuantityInteractor,
    private val productQuantityLazyFlow: Lazy<Flow<ProductQuantity>>
) : MvpPresenter<AmountView>() {

    override fun onFirstViewAttach() {
        getProvidedProductCount()
        viewState.onAmountDefined(BigDecimal.ZERO)
    }

    fun setAmount(value: String) =
        productQuantityInteractor.setAmount(value)

    private fun getProvidedProductCount() {
        productQuantityLazyFlow.get()
            .onEach { viewState.onAmountChanged(it.amount) }
            .launchIn(presenterScope)
    }
}