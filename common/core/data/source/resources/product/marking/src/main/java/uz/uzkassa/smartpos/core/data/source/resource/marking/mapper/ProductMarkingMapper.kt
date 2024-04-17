package uz.uzkassa.smartpos.core.data.source.resource.marking.mapper

import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarking
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarkingEntity

fun ProductMarkingEntity.map(): ProductMarking =
    ProductMarking(id = id, productId = productId, marking = marking)

fun ProductMarking.map(): ProductMarkingEntity =
    ProductMarkingEntity(id = id, productId = productId, marking = marking)

fun List<ProductMarking>.mapToEntity(): List<ProductMarkingEntity> = map { it.map() }

fun List<ProductMarkingEntity>.map(): List<ProductMarking> = map { it.map() }