package uz.uzkassa.smartpos.feature.product.unit.creation.presentation.features

import kotlinx.coroutines.FlowPreview
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.product.unit.creation.data.exception.UnitCreationException
import uz.uzkassa.smartpos.feature.product.unit.creation.domain.ProductUnitInteractor
import uz.uzkassa.smartpos.feature.product.unit.creation.domain.ProductUnitParamsInteractor
import javax.inject.Inject
import kotlin.properties.Delegates

internal class ProductUnitParamsPresenter @Inject constructor(
    private val productUnitParamsInteractor: ProductUnitParamsInteractor,
    private val productUnitInteractor: ProductUnitInteractor
) : MvpPresenter<ProductUnitParamsView>() {
    var isTop: Boolean by Delegates.notNull()
    var unitId: Long by Delegates.notNull()
    var coefficient: Double by Delegates.notNull()
    var unitIndex: Int by Delegates.notNull()

    @FlowPreview
    override fun onFirstViewAttach() {
        productUnitInteractor.setIsTop(isTop)
        productUnitParamsInteractor.setUnitId(unitId)
        productUnitParamsInteractor.setCoefficient(coefficient)
        productUnitParamsInteractor.setUnitIndex(unitIndex)
        productUnitParamsInteractor.setIsTop(isTop)
        getUnits()
    }

    fun sendResult() {
        productUnitParamsInteractor
            .sendResult()
            .launchCatchingIn(presenterScope)
            .onSuccess { dismiss() }
            .onFailure {
                when (it) {
                    is UnitCreationException -> {
                        if (it.isUnitCountNotDefined)
                            viewState.orErrorUnitCountNotDefined()
                        if (it.isUnitNotDefined)
                            viewState.onErrorUnitNotDefined()
                    }
                }
            }
    }

    fun setUnit(unit: Unit) {
        productUnitParamsInteractor.setParentUnit(unit)
        viewState.onUnitDefined(unit)
    }

    fun setProductUnitCount(value: String) =
        productUnitParamsInteractor.setProductUnitCount(value)

    @FlowPreview
    private fun getUnits() {
        productUnitParamsInteractor
            .getUnits()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUnits() }
            .onSuccess {
                viewState.onSuccessUnits(it)
                viewState.onParentUnitNameDefined(productUnitParamsInteractor.getCurrentUnit().name)
            }
            .onFailure { viewState.onErrorUnits(it) }
    }

    fun dismiss() =
        viewState.onDismissView()
}