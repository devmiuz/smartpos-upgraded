package uz.uzkassa.smartpos.trade.presentation.app.di.manager.modules

import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.source.resource.language.preference.LanguagePreference
import uz.uzkassa.smartpos.core.presentation.app.application.ApplicationSupport
import uz.uzkassa.smartpos.core.presentation.app.application.ApplicationSupportDelegate
import javax.inject.Singleton

@Module(includes = [ApplicationModule.Providers::class])
object ApplicationModule {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @Singleton
        fun provideApplicationSupportDelegate(
            applicationSupport: ApplicationSupport,
            languagePreference: LanguagePreference
        ): ApplicationSupportDelegate =
            ApplicationSupportDelegate(applicationSupport, languagePreference)
    }
}