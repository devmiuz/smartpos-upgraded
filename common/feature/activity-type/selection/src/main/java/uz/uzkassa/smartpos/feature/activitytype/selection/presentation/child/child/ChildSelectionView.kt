package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.child

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd
import uz.uzkassa.smartpos.feature.activitytype.selection.data.model.ChildActivityType

internal interface ChildSelectionView : MvpView {

    fun onClearChildActivityTypes()

    fun onLoadingChildActivityTypes()

    fun onSuccessChildActivityTypes(childActivityTypes: List<ChildActivityType>)

    fun onErrorChildActivityTypes(throwable: Throwable)

    @AddToEnd
    fun onChangeChildActivityType(childActivityType: ChildActivityType)
}