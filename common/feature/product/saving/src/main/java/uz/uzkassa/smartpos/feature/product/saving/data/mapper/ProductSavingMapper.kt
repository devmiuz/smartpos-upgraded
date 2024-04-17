package uz.uzkassa.smartpos.feature.product.saving.data.mapper

import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit

internal fun List<Unit>.map() =
    map { it.mapToProductUnit() }

internal fun Unit.mapToProductUnit() =
    ProductUnit(
        id = id,
        productId = null,
        order = 0,
        isBase = true,
        coefficient = 1.0,
        count = 0.0,
        price = null,
        unit = this
    )