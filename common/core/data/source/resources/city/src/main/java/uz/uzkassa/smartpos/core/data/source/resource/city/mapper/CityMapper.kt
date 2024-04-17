package uz.uzkassa.smartpos.core.data.source.resource.city.mapper

import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityResponse

fun City.mapToResponse() =
    CityResponse(
        id = id,
        regionId = regionId,
        nameRu = nameRu,
        nameUz = nameUz
    )