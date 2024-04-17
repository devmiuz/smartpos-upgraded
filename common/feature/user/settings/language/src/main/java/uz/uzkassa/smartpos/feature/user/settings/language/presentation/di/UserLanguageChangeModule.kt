package uz.uzkassa.smartpos.feature.user.settings.language.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.user.settings.language.data.model.LanguageSelection
import uz.uzkassa.smartpos.feature.user.settings.language.data.repository.language.LanguageRepository
import uz.uzkassa.smartpos.feature.user.settings.language.data.repository.language.LanguageRepositoryImpl
import uz.uzkassa.smartpos.feature.user.settings.language.data.repository.user.UserDataRepository
import uz.uzkassa.smartpos.feature.user.settings.language.data.repository.user.UserDataRepositoryImpl

@Module(
    includes = [
        UserLanguageChangeModule.Binders::class,
        UserLanguageChangeModule.Providers::class
    ]
)
internal object UserLanguageChangeModule {

    @Module
    interface Binders {

        @Binds
        @UserLanguageChangeScope
        fun bindLanguageRepository(
            impl: LanguageRepositoryImpl
        ): LanguageRepository

        @Binds
        @UserLanguageChangeScope
        fun bindUserSaveRepository(
            impl: UserDataRepositoryImpl
        ): UserDataRepository
    }

    @Module
    object Providers {

        @Suppress("EXPERIMENTAL_API_USAGE")
        @JvmStatic
        @Provides
        @UserLanguageChangeScope
        fun provideLanguageSelectionBroadcastChannel(): BroadcastChannel<LanguageSelection> =
            BroadcastChannel(1)

        @Suppress("EXPERIMENTAL_API_USAGE")
        @JvmStatic
        @Provides
        @UserLanguageChangeScope
        @FlowPreview
        fun provideLanguageSelectionFlow(
            languageSelectionBroadcastChannel: BroadcastChannel<LanguageSelection>
        ): Flow<LanguageSelection> =
            languageSelectionBroadcastChannel.asFlow()
    }
}