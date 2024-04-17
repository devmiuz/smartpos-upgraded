package uz.uzkassa.smartpos.core.data.source.resource.city.mapper

import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityEntity

fun List<CityEntity>.map() =
    map { it.map() }

fun CityEntity.map() =
    City(
        id = id,
        regionId = regionId,
        nameRu = nameRu,
        nameUz = nameUz
    )