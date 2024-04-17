package uz.uzkassa.smartpos.core.data.source.resource.product.unit.mapper

import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitRelation
import uz.uzkassa.smartpos.core.data.source.resource.unit.mapper.map

fun List<ProductUnitRelation>.map() =
    map { it.map() }

fun ProductUnitRelation.map() =
    ProductUnit(
        id = productUnitEntity.id,
        productId = productUnitEntity.productId,
        order = productUnitEntity.order,
        isBase = productUnitEntity.isBase,
        coefficient = productUnitEntity.coefficient,
        count = productUnitEntity.count,
        price = productUnitEntity.price,
        unit = unitEntity.map()
    )