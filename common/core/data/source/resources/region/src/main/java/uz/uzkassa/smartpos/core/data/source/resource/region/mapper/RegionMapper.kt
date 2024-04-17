package uz.uzkassa.smartpos.core.data.source.resource.region.mapper

import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionResponse

fun Region.mapToResponse() =
    RegionResponse(
        id = id,
        nameRu = nameRu,
        nameUz = nameUz
    )