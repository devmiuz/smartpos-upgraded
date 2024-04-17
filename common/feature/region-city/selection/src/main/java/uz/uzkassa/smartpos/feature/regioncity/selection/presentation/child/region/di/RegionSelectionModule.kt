package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.region.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.regioncity.selection.data.channel.RegionIdBroadcastChannel
import uz.uzkassa.smartpos.feature.regioncity.selection.data.repository.region.RegionRepository
import uz.uzkassa.smartpos.feature.regioncity.selection.data.repository.region.RegionRepositoryImpl
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.city.di.CitySelectionScope

@Module(includes = [RegionSelectionModule.Binders::class, RegionSelectionModule.Providers::class])
internal object RegionSelectionModule {

    @Module
    interface Binders {

        @Binds
        @RegionSelectionScope
        fun bindRegionRepository(
            impl: RegionRepositoryImpl
        ): RegionRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @RegionSelectionScope
        @FlowPreview
        fun provideRegionIdFlow(
            regionIdBroadcastChannel: RegionIdBroadcastChannel
        ): Flow<Long> =
            regionIdBroadcastChannel.asFlow()
    }
}