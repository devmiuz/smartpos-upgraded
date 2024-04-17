package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.parent

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType

internal interface ParentSelectionView : MvpView {

    fun onLoadingActivityTypes()

    fun onSuccessActivityTypes(activityTypes: List<ActivityType>)

    fun onErrorActivityTypes(throwable: Throwable)
}