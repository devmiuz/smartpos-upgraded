package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.quantity

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.core.utils.primitives.roundToString
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.model.ProductQuantity
import uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies.ProductQuantityFeatureArgs
import uz.uzkassa.smartpos.feature.helper.product.quantity.domain.ProductQuantityInteractor
import uz.uzkassa.smartpos.feature.helper.product.quantity.domain.ProductUnitInteractor
import javax.inject.Inject

internal class QuantityPresenter @Inject constructor(
    private val productQuantityInteractor: ProductQuantityInteractor,
    private val productQuantityLazyFlow: Lazy<Flow<ProductQuantity>>,
    private val productUnitInteractor: ProductUnitInteractor,
    private val productQuantityFeatureArgs: ProductQuantityFeatureArgs
) : MvpPresenter<QuantityView>() {

    override fun onFirstViewAttach() {
        getProductUnit()
        getProvidedProductCount()
    }

    fun setQuantity(value: String) {
        productQuantityInteractor.setQuantity(value)
        viewState.onQuantityDefined(value)
    }

    fun setProductUnit(productUnit: ProductUnit) {
        productQuantityInteractor.setProductUnit(productUnit)
        viewState.onUnitChanged(productUnit.unit)
    }

    private fun getProductUnit() {
        productUnitInteractor
            .getProductUnits()
            .launchCatchingIn(presenterScope)
            .onSuccess { list ->
                list?.let { it ->
                    val productUnit: ProductUnit? = it.find { it.unit.id == productQuantityFeatureArgs.unitId }


                    if (productQuantityFeatureArgs.isRefund) {
                        if (productUnit != null) {
                            val refundUnit = productUnit.copy(price = productQuantityFeatureArgs.price)
                            val refundUnits = listOf(refundUnit)
                            productQuantityInteractor.setProductUnits(refundUnits)
                            viewState.onProductUnitsDefined(refundUnits, refundUnit)
                        }
                    } else {
                        productQuantityInteractor.setProductUnits(it)
                        viewState.onProductUnitsDefined(it, productUnit ?: it.find { it.isBase })
                    }

                }
            }
    }

    private fun getProvidedProductCount() {
        productQuantityLazyFlow.get()
            .onEach { viewState.onQuantityDefined(it.quantity.roundToString("#.#####")) }
            .launchIn(presenterScope)
    }
}