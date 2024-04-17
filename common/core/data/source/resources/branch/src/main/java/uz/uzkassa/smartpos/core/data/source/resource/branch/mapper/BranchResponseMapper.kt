package uz.uzkassa.smartpos.core.data.source.resource.branch.mapper

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchEntity
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchResponse
import uz.uzkassa.smartpos.core.data.source.resource.city.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.region.mapper.map

fun List<BranchResponse>.map() =
    map { it.map() }

fun List<BranchResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun BranchResponse.map() =
    Branch(
        id = id,
        isDeleted = isDeleted,
        isFiscal = isFiscal,
        activityType = activityTypeResponse?.map(),
        region = regionResponse?.map(),
        city = cityResponse?.map(),
        name = name,
        description = description,
        address = address
    )

fun BranchResponse.mapToEntity() =
    BranchEntity(
        id = id,
        isDeleted = isDeleted,
        isFiscal = isFiscal,
        activityTypeId = activityTypeResponse?.id,
        regionId = regionResponse?.id,
        cityId = cityResponse?.id,
        name = name,
        description = description,
        address = address
    )