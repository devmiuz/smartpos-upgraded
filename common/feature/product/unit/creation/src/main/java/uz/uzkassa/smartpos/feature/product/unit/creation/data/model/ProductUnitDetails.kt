package uz.uzkassa.smartpos.feature.product.unit.creation.data.model

import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit

data class ProductUnitDetails(
    val parentUnit: Unit?,
    val order: Int,
    val isBase: Boolean,
    val coefficient: Double,
    val count: Double,
    val unit: Unit
)