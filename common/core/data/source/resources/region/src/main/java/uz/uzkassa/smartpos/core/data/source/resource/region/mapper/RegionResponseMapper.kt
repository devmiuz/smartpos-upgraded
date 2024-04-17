package uz.uzkassa.smartpos.core.data.source.resource.region.mapper

import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionEntity
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionResponse

fun List<RegionResponse>.map() =
    map { it.map() }

fun List<RegionResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun RegionResponse.map() =
    Region(
        id = id,
        nameRu = nameRu,
        nameUz = nameUz
    )

fun RegionResponse.mapToEntity() =
    RegionEntity(
        id = id,
        nameRu = nameRu,
        nameUz = nameUz
    )