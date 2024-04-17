package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.parent.di

import dagger.Component
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.parent.ParentSelectionFragment
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.di.ActivityTypeSelectionComponent

@ParentSelectionScope
@Component(dependencies = [ActivityTypeSelectionComponent::class])
internal interface ParentSelectionComponent {

    fun inject(fragment: ParentSelectionFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: ActivityTypeSelectionComponent
        ): ParentSelectionComponent
    }

    companion object {

        fun create(component: ActivityTypeSelectionComponent): ParentSelectionComponent =
            DaggerParentSelectionComponent
                .factory()
                .create(component)
    }
}