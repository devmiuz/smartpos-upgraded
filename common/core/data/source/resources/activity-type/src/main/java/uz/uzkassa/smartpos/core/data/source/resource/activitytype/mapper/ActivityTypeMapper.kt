package uz.uzkassa.smartpos.core.data.source.resource.activitytype.mapper

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeResponse


fun List<ActivityType>.mapToResponses() =
    map { it.mapToResponse() }

fun ActivityType.mapToResponse() =
    ActivityTypeResponse(
        id = id,
        name = name,
        parentId = parentId
    )