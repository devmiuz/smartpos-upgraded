package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.navigation

import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainRouter
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerNavigator
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerScreen
import uz.uzkassa.smartpos.feature.regioncity.selection.dependencies.RegionCitySelectionFeatureCallback

internal class RegionCitySelectionRouter(
    private val regionCitySelectionFeatureCallback: RegionCitySelectionFeatureCallback
) : PlainRouter<ViewPagerNavigator>(ViewPagerNavigator()) {

    fun openRegionSelection() =
        navigateTo(Screens.RegionSelection)

    fun openCitySelection(smoothScroll: Boolean = true) =
        navigateTo(Screens.CitySelection(smoothScroll))

    fun finishSelection(region: Region, city: City) =
        regionCitySelectionFeatureCallback.onFinishRegionCitySelection(region, city)

    object Screens {

        object RegionSelection : ViewPagerScreen() {
            override fun getScreenPosition(): Int? = 1
        }

        class CitySelection(private val smoothScroll: Boolean) : ViewPagerScreen() {
            override fun getScreenPosition(): Int? = 2
            override fun smoothScroll(): Boolean = smoothScroll
        }
    }
}