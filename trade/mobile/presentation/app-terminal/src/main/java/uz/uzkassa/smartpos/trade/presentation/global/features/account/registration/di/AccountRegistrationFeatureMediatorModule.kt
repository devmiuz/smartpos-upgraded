package uz.uzkassa.smartpos.trade.presentation.global.features.account.registration.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.account.registration.dependencies.AccountRegistrationFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.account.registration.AccountRegistrationFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.account.registration.runner.AccountRegistrationFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.browser.runner.BrowserFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        AccountRegistrationFeatureMediatorModule.Binders::class,
        AccountRegistrationFeatureMediatorModule.Providers::class
    ]
)
object AccountRegistrationFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindAccountRegistrationFeatureCallback(
            accountRegistrationFeatureMediator: AccountRegistrationFeatureMediator
        ): AccountRegistrationFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideAccountRegistrationFeatureRunner(
            accountRegistrationFeatureMediator: AccountRegistrationFeatureMediator
        ): AccountRegistrationFeatureRunner =
            accountRegistrationFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideAccountRegistrationFeatureMediator(
            browserFeatureRunner: BrowserFeatureRunner,
            globalRouter: GlobalRouter
        ): AccountRegistrationFeatureMediator =
            AccountRegistrationFeatureMediator(
                browserFeatureRunner,
                globalRouter
            )
    }
}