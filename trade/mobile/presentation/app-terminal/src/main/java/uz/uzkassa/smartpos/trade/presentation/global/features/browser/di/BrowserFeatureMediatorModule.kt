package uz.uzkassa.smartpos.trade.presentation.global.features.browser.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.common.feature.browser.dependencies.BrowserFeatureArgs
import uz.uzkassa.common.feature.browser.dependencies.BrowserFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.browser.BrowserFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.browser.runner.BrowserFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        BrowserFeatureMediatorModule.Binders::class,
        BrowserFeatureMediatorModule.Providers::class
    ]
)
object BrowserFeatureMediatorModule {

    @Module
    interface Binders {
        @Binds
        @GlobalScope
        fun bindBrowserFeatureArgs(
            browserFeatureMediator: BrowserFeatureMediator
        ): BrowserFeatureArgs

        @Binds
        @GlobalScope
        fun bindBrowserFeatureCallback(
            browserFeatureMediator: BrowserFeatureMediator
        ): BrowserFeatureCallback
    }

    @Module
    object Providers {
        @JvmStatic
        @Provides
        @GlobalScope
        fun provideBrowserFeatureRunner(
            browserFeatureMediator: BrowserFeatureMediator
        ): BrowserFeatureRunner =
            browserFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideBrowserFeatureMediator(
            globalRouter: GlobalRouter
        ): BrowserFeatureMediator =
            BrowserFeatureMediator(globalRouter)
    }
}