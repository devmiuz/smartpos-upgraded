package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.amount.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.channel.DiscountArbitraryBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountArbitrary

@Module(includes = [AmountModule.Providers::class])
internal object AmountModule {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @AmountScope
        @FlowPreview
        fun provideSaleDiscountArbitraryFlow(
            discountArbitraryBroadcastChannel: DiscountArbitraryBroadcastChannel
        ): Flow<DiscountArbitrary> =
            discountArbitraryBroadcastChannel.asFlow()
    }
}