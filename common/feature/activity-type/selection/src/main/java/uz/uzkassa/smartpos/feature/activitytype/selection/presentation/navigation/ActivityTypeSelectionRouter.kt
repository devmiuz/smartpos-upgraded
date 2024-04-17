package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.navigation

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainRouter
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerNavigator
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerScreen
import uz.uzkassa.smartpos.feature.activitytype.selection.dependencies.ActivityTypeSelectionFeatureCallback

internal class ActivityTypeSelectionRouter(
    private val activityTypeSelectionFeatureCallback: ActivityTypeSelectionFeatureCallback
) : PlainRouter<ViewPagerNavigator>(ViewPagerNavigator()) {

    fun openParentSelectionScreen() =
        navigateTo(Screens.ParentSelection)

    fun openChildSelectionScreen() =
        navigateTo(Screens.ChildSelection)

    fun finishSelection(activityTypes: List<ActivityType>) =
        activityTypeSelectionFeatureCallback.onFinishActivityTypeSelection(activityTypes)

    object Screens {

        object ParentSelection : ViewPagerScreen() {
            override fun getScreenPosition(): Int? = 1
        }

        object ChildSelection : ViewPagerScreen() {
            override fun getScreenPosition(): Int? = 2
        }
    }
}