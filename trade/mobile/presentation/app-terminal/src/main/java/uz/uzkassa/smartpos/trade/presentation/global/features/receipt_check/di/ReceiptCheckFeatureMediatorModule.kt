package uz.uzkassa.smartpos.trade.presentation.global.features.receipt_check.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.check.dependencies.ReceiptCheckFeatureArgs
import uz.uzkassa.smartpos.feature.check.dependencies.ReceiptCheckFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.receipt_check.ReceiptCheckFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.receipt_check.runner.ReceiptCheckFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        ReceiptCheckFeatureMediatorModule.Binders::class,
        ReceiptCheckFeatureMediatorModule.Providers::class
    ]
)
object ReceiptCheckFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindReceiptCheckFeatureArgs(
            receiptCheckFeatureMediator: ReceiptCheckFeatureMediator
        ): ReceiptCheckFeatureArgs

        @Binds
        @GlobalScope
        fun bindReceiptCheckFeatureCallback(
            receiptCheckFeatureMediator: ReceiptCheckFeatureMediator
        ): ReceiptCheckFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideReceiptCheckFeatureRunner(
            receiptCheckFeatureMediator: ReceiptCheckFeatureMediator
        ): ReceiptCheckFeatureRunner =
            receiptCheckFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideReceiptCheckFeatureMediator(
            router: GlobalRouter
        ): ReceiptCheckFeatureMediator =
            ReceiptCheckFeatureMediator(router)
    }
}