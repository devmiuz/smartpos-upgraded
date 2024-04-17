package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.child.di

import dagger.Component
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.child.ChildSelectionFragment
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.di.ActivityTypeSelectionComponent

@ChildSelectionScope
@Component(
    dependencies = [ActivityTypeSelectionComponent::class],
    modules = [ChildSelectionModule::class]
)
internal interface ChildSelectionComponent {

    fun inject(fragment: ChildSelectionFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: ActivityTypeSelectionComponent
        ): ChildSelectionComponent
    }

    companion object {

        fun create(component: ActivityTypeSelectionComponent): ChildSelectionComponent =
            DaggerChildSelectionComponent
                .factory()
                .create(component)
    }
}