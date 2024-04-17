package uz.uzkassa.smartpos.feature.product.unit.creation.presentation.features

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit

internal interface ProductUnitParamsView : MvpView {

    fun onLoadingUnits()

    fun onSuccessUnits(units: List<Unit>)

    fun onErrorUnits(throwable: Throwable)

    fun onParentUnitNameDefined(unitName: String)

    fun onUnitDefined(unit: Unit)

    fun onDismissView()

    fun orErrorUnitCountNotDefined()

    fun onErrorUnitNotDefined()
}