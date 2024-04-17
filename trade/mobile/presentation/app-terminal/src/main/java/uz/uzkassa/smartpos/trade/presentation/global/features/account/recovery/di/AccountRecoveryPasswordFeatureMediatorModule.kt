package uz.uzkassa.smartpos.trade.presentation.global.features.account.recovery.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.account.recovery.password.dependencies.AccountRecoveryPasswordFeatureArgs
import uz.uzkassa.smartpos.feature.account.recovery.password.dependencies.AccountRecoveryPasswordFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.account.recovery.AccountRecoveryPasswordFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.account.recovery.runner.AccountRecoveryPasswordFeatureRunner

@Module(
    includes = [
        AccountRecoveryPasswordFeatureMediatorModule.Binders::class,
        AccountRecoveryPasswordFeatureMediatorModule.Providers::class
    ]
)
object AccountRecoveryPasswordFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindAccountRecoveryPasswordFeatureArgs(
            accountRecoveryPasswordFeatureMediator: AccountRecoveryPasswordFeatureMediator
        ): AccountRecoveryPasswordFeatureArgs

        @Binds
        @GlobalScope
        fun bindAccountRecoveryPasswordFeatureCallback(
            accountRecoveryPasswordFeatureMediator: AccountRecoveryPasswordFeatureMediator
        ): AccountRecoveryPasswordFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideAccountRecoveryPasswordFeatureRunner(
            accountRecoveryPasswordFeatureMediator: AccountRecoveryPasswordFeatureMediator
        ): AccountRecoveryPasswordFeatureRunner =
            accountRecoveryPasswordFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideAccountRecoveryPasswordFeatureMediator(): AccountRecoveryPasswordFeatureMediator =
            AccountRecoveryPasswordFeatureMediator()
    }
}