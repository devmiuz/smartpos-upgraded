package uz.uzkassa.smartpos.trade.presentation.global.features.account.auth.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.account.auth.dependencies.AccountAuthFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.account.auth.AccountAuthFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.account.auth.runner.AccountAuthFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.account.recovery.runner.AccountRecoveryPasswordFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        AccountAuthFeatureMediatorModule.Binders::class,
        AccountAuthFeatureMediatorModule.Providers::class
    ]
)
object AccountAuthFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindAccountAuthFeatureCallback(
            accountAuthFeatureMediator: AccountAuthFeatureMediator
        ): AccountAuthFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideAccountAuthFeatureRunner(
            accountAuthFeatureMediator: AccountAuthFeatureMediator
        ): AccountAuthFeatureRunner =
            accountAuthFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideAccountAuthFeatureMediator(
            accountRecoveryPasswordFeatureRunner: AccountRecoveryPasswordFeatureRunner,
            globalRouter: GlobalRouter
        ): AccountAuthFeatureMediator =
            AccountAuthFeatureMediator(
                accountRecoveryPasswordFeatureRunner = accountRecoveryPasswordFeatureRunner,
                router = globalRouter
            )
    }
}