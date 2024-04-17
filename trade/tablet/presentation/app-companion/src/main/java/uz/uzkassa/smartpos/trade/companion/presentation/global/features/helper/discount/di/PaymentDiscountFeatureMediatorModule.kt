package uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.discount.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.helper.payment.discount.dependencies.PaymentDiscountFeatureArgs
import uz.uzkassa.smartpos.feature.helper.payment.discount.dependencies.PaymentDiscountFeatureCallback
import uz.uzkassa.smartpos.trade.companion.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.discount.PaymentDiscountFeatureMediator
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.discount.runner.PaymentDiscountFeatureRunner

@Module(
    includes = [
        PaymentDiscountFeatureMediatorModule.Binders::class,
        PaymentDiscountFeatureMediatorModule.Providers::class
    ]
)
object PaymentDiscountFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindDiscountFeatureArgs(
            paymentDiscountFeatureMediator: PaymentDiscountFeatureMediator
        ): PaymentDiscountFeatureArgs

        @Binds
        @GlobalScope
        fun bindDiscountFeatureCallback(
            paymentDiscountFeatureMediator: PaymentDiscountFeatureMediator
        ): PaymentDiscountFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun providePaymentDiscountFeatureRunner(
            paymentDiscountFeatureMediator: PaymentDiscountFeatureMediator
        ): PaymentDiscountFeatureRunner =
            paymentDiscountFeatureMediator.featureRunner


        @JvmStatic
        @Provides
        @GlobalScope
        fun providerPaymentDiscountFeatureMediator(
        ): PaymentDiscountFeatureMediator =
            PaymentDiscountFeatureMediator()
    }
}