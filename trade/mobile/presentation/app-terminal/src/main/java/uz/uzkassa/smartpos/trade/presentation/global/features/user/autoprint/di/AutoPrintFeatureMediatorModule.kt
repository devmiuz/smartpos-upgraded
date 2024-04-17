package uz.uzkassa.smartpos.trade.presentation.global.features.user.autoprint.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.user.autoprint.dependencies.AutoPrintFeatureArgs
import uz.uzkassa.smartpos.feature.user.autoprint.dependencies.AutoPrintFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.user.autoprint.AutoPrintFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.user.autoprint.runner.AutoPrintFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        AutoPrintFeatureMediatorModule.Binders::class,
        AutoPrintFeatureMediatorModule.Providers::class
    ]
)
object AutoPrintFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindAutoPrintFeatureArgs(
            autoPrintFeatureMediator: AutoPrintFeatureMediator
        ): AutoPrintFeatureArgs

        @Binds
        @GlobalScope
        fun bindAutoPrintFeatureCallback(
            autoPrintFeatureMediator: AutoPrintFeatureMediator
        ): AutoPrintFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideAutoPrintFeatureRunner(
            autoPrintFeatureMediator: AutoPrintFeatureMediator
        ): AutoPrintFeatureRunner =
            autoPrintFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideAutoPrintFeatureMediator(
            globalRouter: GlobalRouter
        ): AutoPrintFeatureMediator =
            AutoPrintFeatureMediator(
                router = globalRouter
            )
    }
}