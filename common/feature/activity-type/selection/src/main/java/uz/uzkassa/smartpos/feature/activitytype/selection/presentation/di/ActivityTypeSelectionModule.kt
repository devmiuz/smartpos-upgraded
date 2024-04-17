package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.activitytype.selection.data.channel.ChildActivityTypeBroadcastChannel
import uz.uzkassa.smartpos.feature.activitytype.selection.data.channel.HasChildActivityTypesBroadcastChannel
import uz.uzkassa.smartpos.feature.activitytype.selection.data.channel.ParentActivityTypeIdBroadcastChannel
import uz.uzkassa.smartpos.feature.activitytype.selection.data.repository.ActivityTypeRepository
import uz.uzkassa.smartpos.feature.activitytype.selection.data.repository.ActivityTypeRepositoryImpl
import uz.uzkassa.smartpos.feature.activitytype.selection.domain.ActivityTypeSelectionInteractor

@Module(
    includes = [
        ActivityTypeSelectionModule.Binders::class,
        ActivityTypeSelectionModule.Providers::class
    ]
)
internal object ActivityTypeSelectionModule {

    @Module
    interface Binders {

        @Binds
        @ActivityTypeSelectionScope
        fun bindActivityTypeRepository(
            impl: ActivityTypeRepositoryImpl
        ): ActivityTypeRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @ActivityTypeSelectionScope
        fun provideHasChildActivityTypesBroadcastChannel(): HasChildActivityTypesBroadcastChannel =
            HasChildActivityTypesBroadcastChannel()

        @JvmStatic
        @Provides
        @ActivityTypeSelectionScope
        fun provideActivityTypeSelectionBroadcastChannel(): ChildActivityTypeBroadcastChannel =
            ChildActivityTypeBroadcastChannel()

        @JvmStatic
        @Provides
        @ActivityTypeSelectionScope
        fun provideParentActivityTypeIdBroadcastChannel(): ParentActivityTypeIdBroadcastChannel =
            ParentActivityTypeIdBroadcastChannel()

        @JvmStatic
        @Provides
        @ActivityTypeSelectionScope
        fun provideActivityTypeSelectionInteractor(
            activityTypeRepository: ActivityTypeRepository,
            hasChildActivityTypesBroadcastChannel: HasChildActivityTypesBroadcastChannel,
            childActivityTypeBroadcastChannel: ChildActivityTypeBroadcastChannel,
            parentActivityTypeIdBroadcastChannel: ParentActivityTypeIdBroadcastChannel
        ): ActivityTypeSelectionInteractor =
            ActivityTypeSelectionInteractor(
                activityTypeRepository = activityTypeRepository,
                hasChildActivityTypesBroadcastChannel = hasChildActivityTypesBroadcastChannel,
                childActivityTypeBroadcastChannel = childActivityTypeBroadcastChannel,
                parentActivityTypeIdBroadcastChannel = parentActivityTypeIdBroadcastChannel
            )

        @JvmStatic
        @Provides
        @ActivityTypeSelectionScope
        @FlowPreview
        fun provideHasChildActivityTypesFlow(
            hasChildActivityTypesBroadcastChannel: HasChildActivityTypesBroadcastChannel
        ): Flow<Boolean> =
            hasChildActivityTypesBroadcastChannel.asFlow()
    }
}