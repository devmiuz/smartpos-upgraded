package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.main.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.user.settings.dependencies.UserSettingsFeatureArgs
import uz.uzkassa.smartpos.feature.user.settings.dependencies.UserSettingsFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.data.runner.UserDataChangeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.language.runner.UserLanguageChangeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.main.UserSettingsFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.main.runner.UserSettingsFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.password.runner.UserPasswordChangeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.phone_number.runner.UserPhoneNumberChangeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        UserSettingsFeatureMediatorModule.Binders::class,
        UserSettingsFeatureMediatorModule.Providers::class
    ]
)
object UserSettingsFeatureMediatorModule {
    @Module
    interface Binders {
        @Binds
        @GlobalScope
        fun bindUserSettingsFeatureArgs(
            userSettingsFeatureMediator: UserSettingsFeatureMediator
        ): UserSettingsFeatureArgs

        @Binds
        @GlobalScope
        fun bindUserSettingsFeatureCallback(
            userSettingsFeatureMediator: UserSettingsFeatureMediator
        ): UserSettingsFeatureCallback
    }

    @Module
    object Providers {
        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserSettingsFeatureRunner(
            userSettingsFeatureMediator: UserSettingsFeatureMediator
        ): UserSettingsFeatureRunner =
            userSettingsFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserSettingsFeatureMediator(
            globalRouter: GlobalRouter,
            userDataChangeFeatureRunner: UserDataChangeFeatureRunner,
            userLanguageChangeFeatureRunner: UserLanguageChangeFeatureRunner,
            userPasswordChangeFeatureRunner: UserPasswordChangeFeatureRunner,
            userPhoneNumberChangeFeatureRunner: UserPhoneNumberChangeFeatureRunner
        ): UserSettingsFeatureMediator =
            UserSettingsFeatureMediator(
                router = globalRouter,
                userDataChangeFeatureRunner = userDataChangeFeatureRunner,
                userLanguageChangeFeatureRunner = userLanguageChangeFeatureRunner,
                userPasswordChangeFeatureRunner = userPasswordChangeFeatureRunner,
                userPhoneNumberChangeFeatureRunner = userPhoneNumberChangeFeatureRunner
            )
    }
}