package uz.uzkassa.smartpos.feature.category.main.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.category.main.data.channel.HasEnabledCategoriesBroadcastChannel
import uz.uzkassa.smartpos.feature.category.main.data.repository.MainCategoriesRepository
import uz.uzkassa.smartpos.feature.category.main.data.repository.MainCategoriesRepositoryImpl

@Module(
    includes = [
        MainCategoriesModule.Binders::class,
        MainCategoriesModule.Providers::class
    ]
)
internal object MainCategoriesModule {

    @Module
    interface Binders {

        @Binds
        @MainCategoriesScope
        fun bindMainCategoriesRepository(
            impl: MainCategoriesRepositoryImpl
        ): MainCategoriesRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @MainCategoriesScope
        fun provideHasEnabledCategoriesBroadcastChannel(): HasEnabledCategoriesBroadcastChannel =
            HasEnabledCategoriesBroadcastChannel()

        @FlowPreview
        @JvmStatic
        @Provides
        @MainCategoriesScope
        fun provideHasEnabledCategoriesFlow(
            hasEnabledCategoriesBroadcastChannel: HasEnabledCategoriesBroadcastChannel
        ): Flow<Boolean> = hasEnabledCategoriesBroadcastChannel.asFlow()
    }
}