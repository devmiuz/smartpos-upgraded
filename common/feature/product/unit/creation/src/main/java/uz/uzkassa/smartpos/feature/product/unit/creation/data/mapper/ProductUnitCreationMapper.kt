package uz.uzkassa.smartpos.feature.product.unit.creation.data.mapper

import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.feature.product.unit.creation.data.model.ProductUnitDetails
import uz.uzkassa.smartpos.feature.product.unit.creation.data.model.ProductUnitParams


internal fun ProductUnitParams.mapToDetails() =
    ProductUnitDetails(
        parentUnit = currentUnit,
        order = order,
        isBase = false,
        coefficient = coefficient,
        count = count,
        unit = parentUnit
    )

internal fun List<ProductUnit>.mapToDetails(baseUnit: ProductUnit): List<ProductUnitDetails> {
    return mapIndexed { index, productUnit ->
        val parentUnit: Unit? = when {
            index > 0 -> this[index - 1].unit
            index == 0 && !this[index].isBase -> baseUnit.unit
            else -> null
        }
        productUnit.mapToDetails(parentUnit)
    }
}

internal fun ProductUnit.mapToDetails(parentUnit: Unit?) =
    ProductUnitDetails(
        parentUnit = parentUnit,
        order = order,
        isBase = isBase,
        coefficient = coefficient,
        count = count,
        unit = unit
    )

internal fun List<ProductUnit>.mapToTopDetails(
    baseUnit: ProductUnit
): List<ProductUnitDetails> {
    return mapIndexed { index, productUnit ->
        val parentUnit: Unit? = when {
            index > 0 && size == index + 1 -> {
                baseUnit.unit
            }
            index in 1 until size -> {
                this[index + 1].unit
            }
            index == 0 && !this[index].isBase -> {
                if (size > 1) {
                    this[index + 1].unit
                } else {
                    baseUnit.unit
                }
            }
            else -> null
        }
        productUnit.mapToTopDetails(parentUnit)
    }
}

internal fun ProductUnit.mapToTopDetails(parentUnit: Unit?) =
    ProductUnitDetails(
        parentUnit = unit,
        order = order,
        isBase = isBase,
        coefficient = coefficient,
        count = count,
        unit = parentUnit ?: unit
    )

internal fun List<ProductUnitDetails>.mapToResult() =
    map { it.mapToProductUnits() }

internal fun ProductUnitDetails.mapToProductUnits() =
    ProductUnit(
        id = null,
        productId = null,
        order = order,
        isBase = isBase,
        coefficient = coefficient,
        count = count,
        price = null,
        unit = unit
    )

internal fun List<ProductUnitDetails>.  mapToTopResult() =
    map { it.mapToTopProductUnits() }

internal fun ProductUnitDetails.mapToTopProductUnits() =
    ProductUnit(
        id = null,
        productId = null,
        order = order,
        isBase = isBase,
        coefficient = coefficient,
        count = count,
        price = null,
        unit = parentUnit?:unit
    )
