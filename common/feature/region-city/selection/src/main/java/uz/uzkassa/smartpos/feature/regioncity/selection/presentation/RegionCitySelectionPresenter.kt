package uz.uzkassa.smartpos.feature.regioncity.selection.presentation

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainNavigationScreen
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainRouter
import uz.uzkassa.smartpos.feature.regioncity.selection.domain.RegionCitySelectionInteractor
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.navigation.RegionCitySelectionRouter
import javax.inject.Inject

internal class RegionCitySelectionPresenter @Inject constructor(
    private val regionCitySelectionInteractor: RegionCitySelectionInteractor,
    private val selectionUnitLazyFlow: Lazy<Flow<Unit>>,
    private val regionCitySelectionRouter: RegionCitySelectionRouter
) : MvpPresenter<RegionCitySelectionView>(), PlainRouter.NavigatorObserver {

    override fun onFirstViewAttach() {
        getSelectionUnit()
        regionCitySelectionRouter.let {
            it.addNavigatorObserver(this)
            it.openRegionSelection()
        }
    }

    override fun onObserveNavigation(screen: PlainNavigationScreen) {
        when (screen) {
            is RegionCitySelectionRouter.Screens.RegionSelection ->
                viewState.onVisibleRegionSelectionScreen()
            is RegionCitySelectionRouter.Screens.CitySelection ->
                viewState.onVisibleCitySelectionScreen()
        }
    }

    fun openRegionSelection() =
        regionCitySelectionRouter.openRegionSelection()

    fun dismiss() =
        viewState.onDismissView()

    private fun getSelectionUnit() {
        selectionUnitLazyFlow.get()
            .onEach {
                regionCitySelectionInteractor.getRegionCityPair().let {
                    regionCitySelectionRouter.finishSelection(it.first, it.second)
                }

                viewState.onDismissView()
            }
            .launchIn(presenterScope)
    }

    override fun onDestroy() =
        regionCitySelectionRouter.removeNavigatorObserver(this)
}