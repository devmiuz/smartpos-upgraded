package uz.uzkassa.smartpos.trade.presentation.global.di

import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.source.resource.language.preference.LanguagePreference
import uz.uzkassa.smartpos.core.presentation.app.activity.ActivitySupportDelegate

@Module(includes = [GlobalModule.Providers::class])
object GlobalModule {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        fun provideActivitySupportDelegate(
            languagePreference: LanguagePreference
        ): ActivitySupportDelegate =
            ActivitySupportDelegate(languagePreference)
    }
}