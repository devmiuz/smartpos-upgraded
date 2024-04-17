package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.quantity

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit

internal interface QuantityView : MvpView {

    fun onQuantityDefined(quantity: String)

    fun onProductUnitsDefined(productUnits: List<ProductUnit>, productUnit: ProductUnit?)

    fun onUnitChanged(unit: Unit)
}