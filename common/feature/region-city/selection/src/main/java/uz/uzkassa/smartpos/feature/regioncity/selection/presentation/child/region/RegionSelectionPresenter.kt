package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.region

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.regioncity.selection.data.model.RegionCitySelectionType.CITY
import uz.uzkassa.smartpos.feature.regioncity.selection.domain.RegionCitySelectionInteractor
import uz.uzkassa.smartpos.feature.regioncity.selection.domain.region.RegionInteractor
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.navigation.RegionCitySelectionRouter
import javax.inject.Inject

internal class RegionSelectionPresenter @Inject constructor(
    private val regionCitySelectionInteractor: RegionCitySelectionInteractor,
    private val regionCitySelectionRouter: RegionCitySelectionRouter,
    private val regionInteractor: RegionInteractor,
    private val regionIdLazyFlow: Lazy<Flow<Long>>
) : MvpPresenter<RegionSelectionView>() {

    override fun onFirstViewAttach() {
        getRegionId()
        getRegions()
    }

    fun getRegions() {
        regionInteractor
            .getRegions()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingRegion() }
            .onSuccess { viewState.onSuccessRegion(it) }
            .onFailure { viewState.onErrorRegion(it) }
    }

    fun setRegion(region: Region) {
        regionCitySelectionInteractor.setRegion(region)
        regionCitySelectionRouter.openCitySelection()
    }

    private fun getRegionId() {
        regionIdLazyFlow.get()
            .onEach {
                if (regionCitySelectionInteractor.selectionType == CITY)
                    regionCitySelectionRouter.openCitySelection(smoothScroll = false)
            }
            .launchIn(presenterScope)
    }
}