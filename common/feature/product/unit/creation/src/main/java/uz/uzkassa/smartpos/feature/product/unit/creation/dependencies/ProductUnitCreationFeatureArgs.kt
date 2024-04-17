package uz.uzkassa.smartpos.feature.product.unit.creation.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit

interface ProductUnitCreationFeatureArgs {

    val productUnits: List<ProductUnit>
}