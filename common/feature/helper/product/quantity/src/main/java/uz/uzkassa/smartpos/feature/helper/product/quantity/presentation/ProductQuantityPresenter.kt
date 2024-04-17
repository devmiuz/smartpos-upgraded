package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies.ProductQuantityFeatureCallback
import uz.uzkassa.smartpos.feature.helper.product.quantity.domain.ProductQuantityInteractor
import javax.inject.Inject

internal class ProductQuantityPresenter @Inject constructor(
    private val productQuantityInteractor: ProductQuantityInteractor,
    private val productQuantityFeatureCallback: ProductQuantityFeatureCallback,
    private val unitLazyFlow: Lazy<Flow<Unit>>
) : MvpPresenter<ProductQuantityView>() {

    override fun onFirstViewAttach() {
        viewState.onProductNameDefined(productQuantityInteractor.productName)
        viewState.onUnitChanged(false)
        getProvidedProductUnit()
    }

    fun proceedProductQuantityResult() {
        productQuantityInteractor
            .getProductQuantityResult()
            .launchCatchingIn(presenterScope)
            .onSuccess {
                viewState.onDismissView()
                productQuantityFeatureCallback.onFinishProductQuantity(it)
            }
            .onFailure { viewState.onErrorQuantityCauseNotDefined(it) }
    }

    fun dismiss() {
        viewState.onDismissView()
        productQuantityFeatureCallback.onFinishProductQuantity(null)
    }

    private fun getProvidedProductUnit() {
        unitLazyFlow.get()
            .onEach { viewState.onUnitChanged(it.isCountable) }
            .launchIn(presenterScope)
    }
}