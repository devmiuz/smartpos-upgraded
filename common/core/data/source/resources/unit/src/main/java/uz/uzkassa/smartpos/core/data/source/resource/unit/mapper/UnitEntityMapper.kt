package uz.uzkassa.smartpos.core.data.source.resource.unit.mapper

import  uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitEntity

fun List<UnitEntity>.map() =
    map { it.map() }

fun UnitEntity.map() =
    Unit(
        id = id,
        code = code,
        isCountable = isCountable,
        name = name,
        nameRu = nameRu ?: name,
        nameUz = nameUz,
        description = description
    )