package uz.uzkassa.smartpos.trade.presentation.global.features.activitytype

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.feature.activitytype.selection.dependencies.ActivityTypeSelectionFeatureArgs
import uz.uzkassa.smartpos.feature.activitytype.selection.dependencies.ActivityTypeSelectionFeatureCallback
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.ActivityTypeSelectionFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.activitytype.runner.ActivityTypeSelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.runner.FeatureRunner
import kotlin.properties.Delegates

class ActivityTypeSelectionFeatureRunner : FeatureRunner(),
    ActivityTypeSelectionFeatureCallback, ActivityTypeSelectionFeatureArgs {
    override var activityTypes: List<ActivityType> by Delegates.notNull()
    override var isMultiSelection: Boolean by Delegates.notNull()
    private var finishAction: ((List<ActivityType>) -> Unit) by Delegates.notNull()

    val featureRunner: ActivityTypeSelectionFeatureRunner =
        FeatureRunnerImpl()

    override fun onFinishActivityTypeSelection(activityTypes: List<ActivityType>) =
        finishAction.invoke(activityTypes)

    private inner class FeatureRunnerImpl : ActivityTypeSelectionFeatureRunner {

        override fun run(
            activityTypes: List<ActivityType>,
            isMultiSelection: Boolean,
            action: (Screen) -> Unit
        ) {
            this@ActivityTypeSelectionFeatureRunner.activityTypes = activityTypes
            this@ActivityTypeSelectionFeatureRunner.isMultiSelection = isMultiSelection
            action.invoke(Screens.ActivityTypeSelectionScreen)
        }

        override fun finish(action: (List<ActivityType>) -> Unit): ActivityTypeSelectionFeatureRunner {
            finishAction = action
            return this
        }

    }

    private object Screens {

        object ActivityTypeSelectionScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                ActivityTypeSelectionFragment.newInstance()
        }
    }
}