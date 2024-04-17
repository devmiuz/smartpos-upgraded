package uz.uzkassa.smartpos.core.data.source.resource.branch.mapper

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchEntity
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchRelation
import uz.uzkassa.smartpos.core.data.source.resource.city.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.region.mapper.map

fun List<BranchRelation>.map() =
    map { it.map() }

fun BranchRelation.map() =
    Branch(
        id = branchEntity.id,
        isDeleted = branchEntity.isDeleted,
        isFiscal = branchEntity.isFiscal,
        activityType = activityTypeEntity?.map(),
        region = regionEntity?.map(),
        city = cityEntity?.map(),
        name = branchEntity.name,
        description = branchEntity.description,
        address = branchEntity.address
    )

fun BranchRelation.mapToEntity() =
    BranchEntity(
        id = branchEntity.id,
        isDeleted = branchEntity.isDeleted,
        isFiscal = branchEntity.isFiscal,
        activityTypeId = activityTypeEntity?.id,
        regionId = regionEntity?.id,
        cityId = cityEntity?.id,
        name = branchEntity.name,
        description = branchEntity.description,
        address = branchEntity.address
    )