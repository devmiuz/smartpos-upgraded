package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.child.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNotNull
import uz.uzkassa.smartpos.feature.activitytype.selection.data.channel.ChildActivityTypeBroadcastChannel
import uz.uzkassa.smartpos.feature.activitytype.selection.data.channel.ParentActivityTypeIdBroadcastChannel
import uz.uzkassa.smartpos.feature.activitytype.selection.data.model.ChildActivityType

@Module(includes = [ChildSelectionModule.Providers::class])
internal object ChildSelectionModule {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @ChildSelectionScope
        @FlowPreview
        fun provideChildActivityTypeFlow(
            childActivityTypeBroadcastChannel: ChildActivityTypeBroadcastChannel
        ): Flow<ChildActivityType> =
            childActivityTypeBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @ChildSelectionScope
        @FlowPreview
        fun provideParentActivityTypeIdFlow(
            parentActivityTypeIdBroadcastChannel: ParentActivityTypeIdBroadcastChannel
        ): Flow<Long> =
            parentActivityTypeIdBroadcastChannel.asFlow().filterNotNull()
    }
}