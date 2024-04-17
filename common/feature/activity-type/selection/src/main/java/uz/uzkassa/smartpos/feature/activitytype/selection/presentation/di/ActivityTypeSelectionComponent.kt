package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.activitytype.selection.data.channel.ChildActivityTypeBroadcastChannel
import uz.uzkassa.smartpos.feature.activitytype.selection.data.channel.ParentActivityTypeIdBroadcastChannel
import uz.uzkassa.smartpos.feature.activitytype.selection.data.repository.ActivityTypeRepository
import uz.uzkassa.smartpos.feature.activitytype.selection.dependencies.ActivityTypeSelectionFeatureDependencies
import uz.uzkassa.smartpos.feature.activitytype.selection.domain.ActivityTypeSelectionInteractor
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.ActivityTypeSelectionFragment
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.navigation.ActivityTypeSelectionRouter

@ActivityTypeSelectionScope
@Component(
    dependencies = [ActivityTypeSelectionFeatureDependencies::class],
    modules = [
        ActivityTypeSelectionModule::class,
        ActivityTypeSelectionModuleNavigation::class
    ]
)
abstract class ActivityTypeSelectionComponent : ActivityTypeSelectionFeatureDependencies {

    internal abstract val activityTypeRepository: ActivityTypeRepository

    internal abstract val childActivityTypeBroadcastChannel: ChildActivityTypeBroadcastChannel

    internal abstract val parentActivityTypeIdBroadcastChannel: ParentActivityTypeIdBroadcastChannel

    internal abstract val activityTypeSelectionInteractor: ActivityTypeSelectionInteractor

    internal abstract val activityTypeSelectionRouter: ActivityTypeSelectionRouter

    internal abstract fun inject(fragment: ActivityTypeSelectionFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: ActivityTypeSelectionFeatureDependencies
        ): ActivityTypeSelectionComponent
    }

    internal companion object {

        fun create(
            dependencies: ActivityTypeSelectionFeatureDependencies
        ): ActivityTypeSelectionComponent =
            DaggerActivityTypeSelectionComponent
                .factory()
                .create(dependencies)
    }
}