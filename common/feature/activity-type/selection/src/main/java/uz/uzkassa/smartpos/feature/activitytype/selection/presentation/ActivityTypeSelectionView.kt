package uz.uzkassa.smartpos.feature.activitytype.selection.presentation

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

internal interface ActivityTypeSelectionView : MvpView {

    fun onHasChildActivityTypes(hasChildActivityTypes: Boolean)

    @OneExecution
    fun onDismissView()
}