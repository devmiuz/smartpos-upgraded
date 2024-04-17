package uz.uzkassa.smartpos.feature.account.registration.presentation.di

import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerNavigator
import uz.uzkassa.smartpos.feature.account.registration.dependencies.AccountRegistrationFeatureCallback
import uz.uzkassa.smartpos.feature.account.registration.presentation.navigation.RegistrationRouter

@Module(includes = [AccountRegistrationModuleNavigation.Providers::class])
internal object AccountRegistrationModuleNavigation {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @AccountRegistrationScope
        fun provideRegistrationRouter(
            accountRegistrationFeatureCallback: AccountRegistrationFeatureCallback
        ): RegistrationRouter =
            RegistrationRouter(accountRegistrationFeatureCallback)

        @JvmStatic
        @Provides
        @AccountRegistrationScope
        fun provideViewPagerNavigator(
            registrationRouter: RegistrationRouter
        ): ViewPagerNavigator = registrationRouter.navigator
    }
}