package uz.uzkassa.smartpos.feature.regioncity.selection.presentation

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

internal interface RegionCitySelectionView : MvpView {

    fun onVisibleRegionSelectionScreen()

    fun onVisibleCitySelectionScreen()

    @OneExecution
    fun onDismissView()
}