package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.city.di

import dagger.Component
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.city.CitySelectionFragment
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.di.RegionCitySelectionComponent

@CitySelectionScope
@Component(
    dependencies = [RegionCitySelectionComponent::class],
    modules = [CitySelectionModule::class]
)
internal interface CitySelectionComponent {

    fun inject(fragment: CitySelectionFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: RegionCitySelectionComponent
        ): CitySelectionComponent
    }

    companion object {

        fun create(component: RegionCitySelectionComponent): CitySelectionComponent =
            DaggerCitySelectionComponent
                .factory()
                .create(component)
    }
}