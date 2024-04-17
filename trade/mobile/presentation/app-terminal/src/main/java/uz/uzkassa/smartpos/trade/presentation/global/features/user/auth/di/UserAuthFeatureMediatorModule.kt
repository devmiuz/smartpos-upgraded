package uz.uzkassa.smartpos.trade.presentation.global.features.user.auth.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.user.auth.dependencies.UserAuthFeatureArgs
import uz.uzkassa.smartpos.feature.user.auth.dependencies.UserAuthFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.account.recovery.runner.AccountRecoveryPasswordFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.auth.UserAuthFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.user.auth.runner.UserAuthFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        UserAuthFeatureMediatorModule.Binders::class,
        UserAuthFeatureMediatorModule.Providers::class
    ]
)
object UserAuthFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindUserAuthFeatureArgs(
            userAuthFeatureMediator: UserAuthFeatureMediator
        ): UserAuthFeatureArgs

        @Binds
        @GlobalScope
        fun bindUserAuthFeatureCallback(
            userAuthFeatureMediator: UserAuthFeatureMediator
        ): UserAuthFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserAuthFeatureRunner(
            userAuthFeatureMediator: UserAuthFeatureMediator
        ): UserAuthFeatureRunner =
            userAuthFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideUserAuthFeatureMediator(
            accountRecoveryPasswordFeatureRunner: AccountRecoveryPasswordFeatureRunner,
            globalRouter: GlobalRouter
        ): UserAuthFeatureMediator =
            UserAuthFeatureMediator(
                accountRecoveryPasswordFeatureRunner = accountRecoveryPasswordFeatureRunner,
                router = globalRouter
            )
    }
}