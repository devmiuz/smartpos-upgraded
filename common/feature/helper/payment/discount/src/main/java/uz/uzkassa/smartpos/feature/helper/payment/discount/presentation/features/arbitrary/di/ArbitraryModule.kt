package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.di

import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.channel.DiscountArbitraryBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.channel.DiscountArbitraryResultBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.payment.discount.dependencies.PaymentDiscountFeatureArgs
import uz.uzkassa.smartpos.feature.helper.payment.discount.domain.DiscountArbitraryInteractor

@Module(includes = [ArbitraryModule.Providers::class])
internal object ArbitraryModule {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @ArbitraryScope
        fun provideSaleDiscountArbitraryBroadcastChannel(): DiscountArbitraryBroadcastChannel =
            DiscountArbitraryBroadcastChannel()

        @JvmStatic
        @Provides
        @ArbitraryScope
        fun provideDiscountArbitraryInteractor(
            discountArbitraryBroadcastChannel: DiscountArbitraryBroadcastChannel,
            discountArbitraryResultBroadcastChannel: DiscountArbitraryResultBroadcastChannel,
            paymentDiscountFeatureArgs: PaymentDiscountFeatureArgs
        ): DiscountArbitraryInteractor =
            DiscountArbitraryInteractor(
                discountArbitraryBroadcastChannel,
                discountArbitraryResultBroadcastChannel,
                paymentDiscountFeatureArgs
            )
    }
}