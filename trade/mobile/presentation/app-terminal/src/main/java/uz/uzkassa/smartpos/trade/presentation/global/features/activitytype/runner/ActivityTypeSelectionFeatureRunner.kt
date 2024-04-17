package uz.uzkassa.smartpos.trade.presentation.global.features.activitytype.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface ActivityTypeSelectionFeatureRunner : FeatureRunner {

    fun run(activityTypes: List<ActivityType>, isMultiSelection: Boolean, action: (Screen) -> Unit)

    fun finish(action: (List<ActivityType>) -> Unit): ActivityTypeSelectionFeatureRunner
}