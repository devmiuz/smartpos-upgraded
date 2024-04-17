package uz.uzkassa.smartpos.feature.product.unit.creation.presentation

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.feature.product.unit.creation.data.model.ProductUnitDetails
import uz.uzkassa.smartpos.feature.product.unit.creation.dependencies.ProductUnitCreationFeatureCallback
import uz.uzkassa.smartpos.feature.product.unit.creation.domain.ProductUnitCreationInteractor
import uz.uzkassa.smartpos.feature.product.unit.creation.domain.ProductUnitInteractor
import javax.inject.Inject

internal class ProductUnitCreationPresenter @Inject constructor(
    private val productUnitsLazyFlow: Lazy<Flow<List<ProductUnitDetails>>>,
    private val productUnitCreationInteractor: ProductUnitCreationInteractor,
    private val productUnitInteractor: ProductUnitInteractor,
    private val productUnitCreationFeatureCallback: ProductUnitCreationFeatureCallback
) : MvpPresenter<ProductUnitCreationView>() {

    override fun onFirstViewAttach() {
        getUnitList()
        getProvidedProductUnits()
    }

    fun onBottomClicked(details: ProductUnitDetails) {
        productUnitInteractor.getProductDetailsItem(details)
        viewState.onBottomClicked(details)
    }

    fun onTopClicked(details: ProductUnitDetails) {
        productUnitInteractor.getProductDetailsItem(details)
        viewState.onTopClicked(details)
    }

    private fun getUnitList() {
        val units = productUnitCreationInteractor.setUnitDetails()
        viewState.onSuccessProductUnitDetails(units)
    }

    private fun getProvidedProductUnits() {
        productUnitsLazyFlow.get()
            .onEach {
                viewState.onSuccessProductUnitDetails(it)
            }
            .launchIn(presenterScope)
    }

    fun saveUnits() {
        val result = productUnitInteractor.getProductUnits()
        productUnitCreationFeatureCallback.onFinishProductUnitCreation(result)
        viewState.onDismissView()
    }

    fun removeProductUnit(value: ProductUnitDetails) {
        val list = productUnitCreationInteractor.removeProductUnit(value)
        viewState.onSuccessProductUnitDeleted(list)
    }

    fun dismiss() =
        viewState.onDismissView()
}

