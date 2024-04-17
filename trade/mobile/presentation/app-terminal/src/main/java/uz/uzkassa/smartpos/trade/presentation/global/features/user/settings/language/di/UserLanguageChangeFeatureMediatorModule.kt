package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.language.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.user.settings.language.dependencies.UserLanguageChangeFeatureArgs
import uz.uzkassa.smartpos.feature.user.settings.language.dependencies.UserLanguageChangeFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.language.UserLanguageChangeFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.language.runner.UserLanguageChangeFeatureRunner

@Module(
    includes = [
        UserLanguageChangeFeatureMediatorModule.Binders::class,
        UserLanguageChangeFeatureMediatorModule.Providers::class
    ]
)
object UserLanguageChangeFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindUserLanguageChangeFeatureArgs(
            userLanguageChangeFeatureMediator: UserLanguageChangeFeatureMediator
        ): UserLanguageChangeFeatureArgs

        @Binds
        @GlobalScope
        fun bindUserLanguageChangeFeatureCallback(
            userLanguageChangeFeatureMediator: UserLanguageChangeFeatureMediator
        ): UserLanguageChangeFeatureCallback

    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserLanguageChangeFeatureRunner(
            userLanguageChageFeatureMediator: UserLanguageChangeFeatureMediator
        ): UserLanguageChangeFeatureRunner = userLanguageChageFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserLanguageChangeFeatureMediator(): UserLanguageChangeFeatureMediator =
            UserLanguageChangeFeatureMediator()


    }
}