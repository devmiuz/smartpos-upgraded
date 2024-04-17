package uz.uzkassa.smartpos.feature.activitytype.selection.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType

interface ActivityTypeSelectionFeatureCallback {

    fun onFinishActivityTypeSelection(activityTypes: List<ActivityType>)
}