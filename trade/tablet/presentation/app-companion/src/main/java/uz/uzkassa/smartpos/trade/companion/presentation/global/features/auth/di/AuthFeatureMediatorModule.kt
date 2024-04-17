package uz.uzkassa.smartpos.trade.companion.presentation.global.features.auth.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.trade.auth.dependencies.AuthFeatureArgs
import uz.uzkassa.smartpos.trade.auth.dependencies.AuthFeatureCallback
import uz.uzkassa.smartpos.trade.companion.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.auth.AuthFeatureMediator
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.auth.runner.AuthFeatureRunner
import uz.uzkassa.smartpos.trade.companion.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        AuthFeatureMediatorModule.Binders::class,
        AuthFeatureMediatorModule.Providers::class
    ]
)
object AuthFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindAuthFeatureArgs(
            authFeatureMediator: AuthFeatureMediator
        ): AuthFeatureArgs

        @Binds
        @GlobalScope
        fun bindAuthFeatureCallback(
            authFeatureMediator: AuthFeatureMediator
        ): AuthFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideAuthFeatureRunner(
            authFeatureMediator: AuthFeatureMediator
        ): AuthFeatureRunner =
            authFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideAuthFeatureMediator(
            globalRouter: GlobalRouter
        ): AuthFeatureMediator =
            AuthFeatureMediator(globalRouter)
    }
}