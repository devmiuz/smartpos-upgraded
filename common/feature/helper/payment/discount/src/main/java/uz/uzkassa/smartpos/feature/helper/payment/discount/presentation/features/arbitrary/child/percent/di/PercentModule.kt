package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.child.percent.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.channel.DiscountArbitraryBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountArbitrary

@Module(includes = [PercentModule.Providers::class])
internal object PercentModule {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @PercentScope
        @FlowPreview
        fun provideSaleDiscountArbitraryFlow(
            discountArbitraryBroadcastChannel: DiscountArbitraryBroadcastChannel
        ): Flow<DiscountArbitrary> =
            discountArbitraryBroadcastChannel.asFlow()
    }
}