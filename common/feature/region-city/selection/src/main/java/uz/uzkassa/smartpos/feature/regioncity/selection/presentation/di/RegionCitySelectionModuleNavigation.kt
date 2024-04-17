package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.di

import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerNavigator
import uz.uzkassa.smartpos.feature.regioncity.selection.dependencies.RegionCitySelectionFeatureCallback
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.navigation.RegionCitySelectionRouter

@Module(includes = [RegionCitySelectionModuleNavigation.Providers::class])
internal object RegionCitySelectionModuleNavigation {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @RegionCitySelectionScope
        fun provideRegionCitySelectionRouter(
            regionCitySelectionFeatureCallback: RegionCitySelectionFeatureCallback
        ): RegionCitySelectionRouter =
            RegionCitySelectionRouter(regionCitySelectionFeatureCallback)

        @JvmStatic
        @Provides
        @RegionCitySelectionScope
        fun provideViewPagerNavigator(
            regionCitySelectionRouter: RegionCitySelectionRouter
        ): ViewPagerNavigator = regionCitySelectionRouter.navigator
    }
}