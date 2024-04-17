package uz.uzkassa.smartpos.feature.product.unit.creation.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit

interface ProductUnitCreationFeatureCallback {

    fun onFinishProductUnitCreation(list: List<ProductUnit>)
}