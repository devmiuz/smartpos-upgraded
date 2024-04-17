package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.password.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.user.settings.password.dependencies.UserPasswordChangeFeatureArgs
import uz.uzkassa.smartpos.feature.user.settings.password.dependencies.UserPasswordChangeFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.password.UserPasswordChangeFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.password.runner.UserPasswordChangeFeatureRunner

@Module(
    includes = [
        UserPasswordChangeFeatureMediatorModule.Binders::class,
        UserPasswordChangeFeatureMediatorModule.Providers::class
    ]
)
object UserPasswordChangeFeatureMediatorModule {

    @Module
    interface Binders {
        @Binds
        @GlobalScope
        fun bindUserPasswordChangeFeatureArgs(
            userPasswordChangeFeatureMediator: UserPasswordChangeFeatureMediator
        ): UserPasswordChangeFeatureArgs

        @Binds
        @GlobalScope
        fun bindUserPasswordChangeFeatureCallback(
            userPasswordChangeFeatureMediator: UserPasswordChangeFeatureMediator
        ): UserPasswordChangeFeatureCallback
    }

    @Module
    object Providers {
        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserPasswordChangeFeatureRunner(
            userPasswordChangeFeatureMediator: UserPasswordChangeFeatureMediator
        ): UserPasswordChangeFeatureRunner = userPasswordChangeFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserPasswordChangeFeatureMediator(): UserPasswordChangeFeatureMediator =
            UserPasswordChangeFeatureMediator()
    }
}