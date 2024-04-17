package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.city.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.regioncity.selection.data.channel.RegionIdBroadcastChannel
import uz.uzkassa.smartpos.feature.regioncity.selection.data.repository.city.CityRepository
import uz.uzkassa.smartpos.feature.regioncity.selection.data.repository.city.CityRepositoryImpl

@Module(includes = [CitySelectionModule.Binders::class, CitySelectionModule.Providers::class])
internal object CitySelectionModule {

    @Module
    interface Binders {

        @Binds
        @CitySelectionScope
        fun bindCityRepository(
            impl: CityRepositoryImpl
        ): CityRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @CitySelectionScope
        @FlowPreview
        fun provideRegionIdFlow(
            regionIdBroadcastChannel: RegionIdBroadcastChannel
        ): Flow<Long> = regionIdBroadcastChannel.asFlow()
    }
}