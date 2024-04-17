package uz.uzkassa.smartpos.core.data.source.resource.product.unit.mapper

import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitEntity
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitResponse

fun List<ProductUnitResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun List<ProductUnit>.mapToResponses() =
    map { it.mapToResponse() }

fun ProductUnitResponse.mapToEntity() =
    ProductUnitEntity(
        id = checkNotNull(id),
        unitId = unitId,
        productId = checkNotNull(productId),
        order = order,
        isBase = isBase,
        coefficient = coefficient,
        count = count,
        price = price
    )

fun ProductUnit.mapToResponse() =
    ProductUnitResponse(
        id = id,
        unitId = unit.id,
        productId = productId,
        order = order,
        isBase = isBase,
        coefficient = coefficient,
        count = count,
        price = price
    )
