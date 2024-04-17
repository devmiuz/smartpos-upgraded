package uz.uzkassa.smartpos.feature.activitytype.selection.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType

interface ActivityTypeSelectionFeatureArgs {

    val activityTypes: List<ActivityType>

    val isMultiSelection: Boolean
}