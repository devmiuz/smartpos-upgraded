package uz.uzkassa.smartpos.feature.account.recovery.password.presentation.di

import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerNavigator
import uz.uzkassa.smartpos.feature.account.recovery.password.dependencies.AccountRecoveryPasswordFeatureCallback
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.navigation.RecoveryPasswordRouter

@Module(includes = [AccountRecoveryPasswordModuleNavigation.Providers::class])
internal object AccountRecoveryPasswordModuleNavigation {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @AccountRecoveryPasswordScope
        fun providePasswordRecoveryRouter(
            accountRecoveryPasswordFeatureCallback: AccountRecoveryPasswordFeatureCallback
        ): RecoveryPasswordRouter =
            RecoveryPasswordRouter(accountRecoveryPasswordFeatureCallback)

        @JvmStatic
        @Provides
        @AccountRecoveryPasswordScope
        fun provideViewPagerNavigator(
            recoveryPasswordRouter: RecoveryPasswordRouter
        ): ViewPagerNavigator = recoveryPasswordRouter.navigator
    }
}