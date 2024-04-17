package uz.uzkassa.smartpos.core.data.source.resource.activitytype.mapper

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeEntity

fun List<ActivityTypeEntity>.map() =
    map { it.map() }

fun ActivityTypeEntity.map() =
    ActivityType(
        id = id,
        parentId = parentId,
        name = name
    )