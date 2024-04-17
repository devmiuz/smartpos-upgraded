package uz.uzkassa.smartpos.core.data.source.resource.unit.mapper

import  uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitEntity
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitResponse

fun List<UnitResponse>.map() =
    map { it.map() }

fun List<UnitResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun Unit.mapToResponse() =
    UnitResponse(
        id = id,
        code = code,
        isCountable = isCountable,
        name = name,
        nameRu = nameRu,
        nameUz = nameUz,
        description = description
    )

fun UnitResponse.map() =
    Unit(
        id = id,
        code = code,
        isCountable = isCountable,
        name = name,
        nameRu = nameRu ?: name,
        nameUz = nameUz,
        description = description
    )

fun UnitResponse.mapToEntity() =
    UnitEntity(
        id = id,
        code = code,
        isCountable = isCountable,
        name = name,
        nameRu = nameRu,
        nameUz = nameUz,
        description = description
    )