package uz.uzkassa.smartpos.core.data.source.resource.activitytype.mapper

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeResponse

fun List<ActivityTypeResponse>.map() =
    map { it.map() }

fun List<ActivityTypeResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun ActivityTypeResponse.map() =
    ActivityType(
        id = id,
        parentId = parentId,
        name = name
    )

fun ActivityTypeResponse.mapToEntity() =
    ActivityTypeEntity(
        id = id,
        parentId = parentId,
        name = name
    )