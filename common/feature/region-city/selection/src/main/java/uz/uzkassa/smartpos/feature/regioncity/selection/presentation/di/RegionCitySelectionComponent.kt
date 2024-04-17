package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.regioncity.selection.data.channel.RegionIdBroadcastChannel
import uz.uzkassa.smartpos.feature.regioncity.selection.dependencies.RegionCitySelectionFeatureDependencies
import uz.uzkassa.smartpos.feature.regioncity.selection.domain.RegionCitySelectionInteractor
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.RegionCitySelectionFragment
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.navigation.RegionCitySelectionRouter

@RegionCitySelectionScope
@Component(
    dependencies = [RegionCitySelectionFeatureDependencies::class],
    modules = [
        RegionCitySelectionModule::class,
        RegionCitySelectionModuleNavigation::class
    ]
)
abstract class RegionCitySelectionComponent : RegionCitySelectionFeatureDependencies {

    internal abstract val regionCitySelectionInteractor: RegionCitySelectionInteractor

    internal abstract val regionIdBroadcastChannel: RegionIdBroadcastChannel

    internal abstract val regionCitySelectionRouter: RegionCitySelectionRouter

    internal abstract fun inject(fragment: RegionCitySelectionFragment)

    @Component.Factory
    interface Factory {
        fun create(
            dependencies: RegionCitySelectionFeatureDependencies
        ): RegionCitySelectionComponent
    }

    internal companion object {

        fun create(
            dependencies: RegionCitySelectionFeatureDependencies
        ): RegionCitySelectionComponent =
            DaggerRegionCitySelectionComponent
                .factory()
                .create(dependencies)
    }
}