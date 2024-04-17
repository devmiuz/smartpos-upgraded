package uz.uzkassa.smartpos.core.data.source.resource.region.mapper

import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionEntity

fun List<RegionEntity>.map() =
    map { it.map() }

fun RegionEntity.map() =
    Region(
        id = id,
        nameRu = nameRu,
        nameUz = nameUz
    )