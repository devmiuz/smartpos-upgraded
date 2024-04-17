package uz.uzkassa.smartpos.core.data.source.resource.product.unit.model

import androidx.room.Embedded
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitEntity

data class ProductUnitRelation(
    @Embedded
    val productUnitEntity: ProductUnitEntity,

    @Embedded
    val unitEntity: UnitEntity
)