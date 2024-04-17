package uz.uzkassa.smartpos.feature.activitytype.selection.data.model

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType

internal data class ChildActivityType(val activityType: ActivityType, val isSelected: Boolean)