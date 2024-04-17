package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.regioncity.selection.data.channel.RegionIdBroadcastChannel
import uz.uzkassa.smartpos.feature.regioncity.selection.data.channel.SelectionUnitBroadcastChannel
import uz.uzkassa.smartpos.feature.regioncity.selection.dependencies.RegionCitySelectionFeatureArgs
import uz.uzkassa.smartpos.feature.regioncity.selection.domain.RegionCitySelectionInteractor

@Module(includes = [RegionCitySelectionModule.Providers::class])
internal object RegionCitySelectionModule {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @RegionCitySelectionScope
        fun provideRegionIdBroadcastChannel(): RegionIdBroadcastChannel =
            RegionIdBroadcastChannel()

        @JvmStatic
        @Provides
        @RegionCitySelectionScope
        fun provideSelectionUnitBroadcastChannel(): SelectionUnitBroadcastChannel =
            SelectionUnitBroadcastChannel()

        @JvmStatic
        @Provides
        @RegionCitySelectionScope
        fun provideRegionCitySelectionInteractor(
            regionCitySelectionFeatureArgs: RegionCitySelectionFeatureArgs,
            regionIdBroadcastChannel: RegionIdBroadcastChannel,
            selectionUnitBroadcastChannel: SelectionUnitBroadcastChannel
        ): RegionCitySelectionInteractor =
            RegionCitySelectionInteractor(
                regionCitySelectionFeatureArgs = regionCitySelectionFeatureArgs,
                regionIdBroadcastChannel = regionIdBroadcastChannel,
                selectionUnitBroadcastChannel = selectionUnitBroadcastChannel
            )

        @JvmStatic
        @Provides
        @RegionCitySelectionScope
        @FlowPreview
        fun provideSelectionUnitFlow(
            selectionUnitBroadcastChannel: SelectionUnitBroadcastChannel
        ): Flow<Unit> =
            selectionUnitBroadcastChannel.asFlow()
    }
}