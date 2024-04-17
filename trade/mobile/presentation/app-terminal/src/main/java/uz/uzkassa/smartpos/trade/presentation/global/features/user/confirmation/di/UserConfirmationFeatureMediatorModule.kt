package uz.uzkassa.smartpos.trade.presentation.global.features.user.confirmation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.user.confirmation.dependencies.UserConfirmationFeatureArgs
import uz.uzkassa.smartpos.feature.user.confirmation.dependencies.UserConfirmationFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.user.confirmation.UserConfirmationFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.user.confirmation.runner.UserConfirmationFeatureRunner

@Module(
    includes = [
        UserConfirmationFeatureMediatorModule.Binders::class,
        UserConfirmationFeatureMediatorModule.Providers::class
    ]
)
object UserConfirmationFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindUserConfirmationFeatureArgs(
            userConfirmationFeatureMediator: UserConfirmationFeatureMediator
        ): UserConfirmationFeatureArgs

        @Binds
        @GlobalScope
        fun bindUserConfirmationFeatureCallback(
            userConfirmationFeatureMediator: UserConfirmationFeatureMediator
        ): UserConfirmationFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserConfirmationFeatureRunner(
            userConfirmationFeatureMediator: UserConfirmationFeatureMediator
        ): UserConfirmationFeatureRunner =
            userConfirmationFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserConfirmationFeatureMediator(): UserConfirmationFeatureMediator =
            UserConfirmationFeatureMediator()
    }
}