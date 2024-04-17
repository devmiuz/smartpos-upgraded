package uz.uzkassa.smartpos.trade.presentation.global.features.helper.amount.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.helper.payment.amount.dependencies.PaymentAmountFeatureArgs
import uz.uzkassa.smartpos.feature.helper.payment.amount.dependencies.PaymentAmountFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.amount.PaymentAmountFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.amount.runner.PaymentAmountFeatureRunner

@Module(
    includes = [
        PaymentAmountFeatureMediatorModule.Binders::class,
        PaymentAmountFeatureMediatorModule.Providers::class
    ]
)
object PaymentAmountFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindAmountFeatureArgs(
            paymentAmountFeatureMediator: PaymentAmountFeatureMediator
        ): PaymentAmountFeatureArgs

        @Binds
        @GlobalScope
        fun bindAmountFeatureCallback(
            paymentAmountFeatureMediator: PaymentAmountFeatureMediator
        ): PaymentAmountFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun providePaymentAmountFeatureRunner(
            paymentAmountFeatureMediator: PaymentAmountFeatureMediator
        ): PaymentAmountFeatureRunner =
            paymentAmountFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun providePaymentAmountFeatureMediator(
        ): PaymentAmountFeatureMediator =
            PaymentAmountFeatureMediator()
    }
}