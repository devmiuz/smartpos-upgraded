package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.region.di

import dagger.Component
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.region.RegionSelectionFragment
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.di.RegionCitySelectionComponent

@RegionSelectionScope
@Component(
    dependencies = [RegionCitySelectionComponent::class],
    modules = [RegionSelectionModule::class]
)
internal interface RegionSelectionComponent {

    fun inject(fragment: RegionSelectionFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: RegionCitySelectionComponent
        ): RegionSelectionComponent
    }

    companion object {

        fun create(component: RegionCitySelectionComponent): RegionSelectionComponent =
            DaggerRegionSelectionComponent
                .factory()
                .create(component)
    }
}