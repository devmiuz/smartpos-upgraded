package uz.uzkassa.smartpos.feature.product.unit.creation.data.model

import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit

internal data class ProductUnitParams(
    val currentUnit: Unit,
    val count: Double = 1.0,
    val coefficient: Double = 1.0,
    val parentUnit: Unit,
    val order: Int
)