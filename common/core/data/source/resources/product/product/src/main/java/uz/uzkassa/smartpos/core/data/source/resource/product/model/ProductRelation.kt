package uz.uzkassa.smartpos.core.data.source.resource.product.model

import androidx.room.Embedded
import androidx.room.Ignore
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryEntity
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitRelation
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitEntity

data class ProductRelation @Ignore internal constructor(
    @Embedded
    val categoryEntity: CategoryEntity?,

    @Embedded
    val productEntity: ProductEntity,

    @Embedded
    val unitEntity: UnitEntity?,

    @Ignore
    val productUnitRelations: List<ProductUnitRelation>
) {

    internal constructor(
        categoryEntity: CategoryEntity?,
        productEntity: ProductEntity,
        unitEntity: UnitEntity?
    ): this(
        categoryEntity = categoryEntity,
        productEntity = productEntity,
        unitEntity = unitEntity,
        productUnitRelations = emptyList()
    )
}