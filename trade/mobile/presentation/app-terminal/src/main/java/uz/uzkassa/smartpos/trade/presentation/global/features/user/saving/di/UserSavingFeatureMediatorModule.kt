package uz.uzkassa.smartpos.trade.presentation.global.features.user.saving.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.user.saving.dependencies.UserSavingFeatureArgs
import uz.uzkassa.smartpos.feature.user.saving.dependencies.UserSavingFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.user.saving.UserSavingFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.user.saving.runner.UserSavingFeatureRunner

@Module(
    includes = [
        UserSavingFeatureMediatorModule.Binders::class,
        UserSavingFeatureMediatorModule.Providers::class
    ]
)
object UserSavingFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindUserSavingFeatureArgs(
            userSavingFeatureMediator: UserSavingFeatureMediator
        ): UserSavingFeatureArgs

        @Binds
        @GlobalScope
        fun bindUserSavingFeatureCallback(
            userSavingFeatureMediator: UserSavingFeatureMediator
        ): UserSavingFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserSavingFeatureRunner(
            userSavingFeatureMediator: UserSavingFeatureMediator
        ): UserSavingFeatureRunner =
            userSavingFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserSavingFeatureMediator(): UserSavingFeatureMediator =
            UserSavingFeatureMediator()
    }
}