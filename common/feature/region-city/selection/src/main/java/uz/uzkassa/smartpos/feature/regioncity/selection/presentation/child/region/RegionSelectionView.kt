package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.region

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region

internal interface RegionSelectionView : MvpView {

    fun onLoadingRegion()

    fun onSuccessRegion(regions: List<Region>)

    fun onErrorRegion(throwable: Throwable)
}