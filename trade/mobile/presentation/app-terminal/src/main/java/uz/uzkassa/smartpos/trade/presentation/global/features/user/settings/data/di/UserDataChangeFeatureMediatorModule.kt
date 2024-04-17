package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.user.settings.data.dependencies.UserDataChangeFeatureArgs
import uz.uzkassa.smartpos.feature.user.settings.data.dependencies.UserDataChangeFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.data.UserDataChangeFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.data.runner.UserDataChangeFeatureRunner

@Module(
    includes = [
        UserDataChangeFeatureMediatorModule.Binders::class,
        UserDataChangeFeatureMediatorModule.Providers::class
    ]
)
object UserDataChangeFeatureMediatorModule {

    @Module
    interface Binders {
        @Binds
        @GlobalScope
        fun bindUserDataChangeFeatureArgs(
            userDataChangeFeatureMediator: UserDataChangeFeatureMediator
        ): UserDataChangeFeatureArgs

        @Binds
        @GlobalScope
        fun bindUserDataChangeFeatureCallback(
            userDataChangeFeatureMediator: UserDataChangeFeatureMediator
        ): UserDataChangeFeatureCallback
    }

    @Module
    object Providers {
        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserDataChangeFeatureRunner(
            userDataChangeFeatureMediator: UserDataChangeFeatureMediator
        ): UserDataChangeFeatureRunner = userDataChangeFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserDataChangeFeatureMediator(): UserDataChangeFeatureMediator =
            UserDataChangeFeatureMediator()
    }
}