package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.channel.DiscountArbitraryResultBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountArbitrary
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.repository.CardRepository
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.repository.CardRepositoryImpl

@Module(includes = [PaymentDiscountModule.Binders::class, PaymentDiscountModule.Providers::class])
internal object PaymentDiscountModule {

    @Module
    interface Binders {

        @Binds
        @PaymentDiscountScope
        fun bindCardRepository(
            impl: CardRepositoryImpl
        ): CardRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @PaymentDiscountScope
        fun provideDiscountArbitraryResultBroadcastChannel(): DiscountArbitraryResultBroadcastChannel =
            DiscountArbitraryResultBroadcastChannel()

        @JvmStatic
        @Provides
        @PaymentDiscountScope
        @FlowPreview
        fun provideDiscountArbitraryFlow(
            discountArbitraryResultBroadcastChannel: DiscountArbitraryResultBroadcastChannel
        ): Flow<DiscountArbitrary> =
            discountArbitraryResultBroadcastChannel.asFlow()
    }
}