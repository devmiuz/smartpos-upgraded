package uz.uzkassa.smartpos.trade.presentation.global.features.region_city.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.regioncity.selection.dependencies.RegionCitySelectionFeatureArgs
import uz.uzkassa.smartpos.feature.regioncity.selection.dependencies.RegionCitySelectionFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.region_city.RegionCitySelectionFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.region_city.runner.RegionCitySelectionFeatureRunner

@Module(
    includes = [
        RegionCitySelectionFeatureMediatorModule.Binders::class,
        RegionCitySelectionFeatureMediatorModule.Providers::class
    ]
)
object RegionCitySelectionFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindRegionCitySelectionFeatureArgs(
            regionCitySelectionFeatureMediator: RegionCitySelectionFeatureMediator
        ): RegionCitySelectionFeatureArgs

        @Binds
        @GlobalScope
        fun bindRegionCitySelectionFeatureCallback(
            regionCitySelectionFeatureMediator: RegionCitySelectionFeatureMediator
        ): RegionCitySelectionFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideRegionCitySelectionFeatureRunner(
            regionCitySelectionFeatureMediator: RegionCitySelectionFeatureMediator
        ): RegionCitySelectionFeatureRunner =
            regionCitySelectionFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideRegionCitySelectionFeatureMediator(): RegionCitySelectionFeatureMediator =
            RegionCitySelectionFeatureMediator()
    }
}