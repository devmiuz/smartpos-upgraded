package uz.uzkassa.smartpos.core.data.source.resource.city.mapper

import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityEntity
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityResponse

fun List<CityResponse>.map() =
    map { it.map() }

fun List<CityResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun CityResponse.map() =
    City(
        id = id,
        regionId = regionId,
        nameRu = nameRu,
        nameUz = nameUz
    )

fun CityResponse.mapToEntity() =
    CityEntity(
        id = id,
        regionId = regionId,
        nameRu = nameRu,
        nameUz = nameUz
    )