package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.cart.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.product_marking.data.model.ProductMarkingResult
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.cart.RefundCartBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.product.ProductMarkingResultBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.cart.RefundCart
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.quantity.ProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.cash.CashOperationsDetailsRepository
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.cash.CashOperationsDetailsRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies.CashierRefundFeatureArgs

@Module(includes = [RefundCartModule.Binders::class, RefundCartModule.Providers::class])
internal object RefundCartModule {

    @Module
    object Providers {

        @ExperimentalCoroutinesApi
        @JvmStatic
        @Provides
        @RefundCartScope
        @FlowPreview
        fun provideProductQuantityFlow(
            cashierRefundFeatureArgs: CashierRefundFeatureArgs
        ): Flow<ProductQuantity> =
            cashierRefundFeatureArgs.productQuantityBroadcastChannel.asFlow()

        @ExperimentalCoroutinesApi
        @JvmStatic
        @Provides
        @RefundCartScope
        @FlowPreview
        fun provideRefundCartFlow(
            refundCartBroadcastChannel: RefundCartBroadcastChannel
        ): Flow<RefundCart> =
            refundCartBroadcastChannel.asFlow()

        @ExperimentalCoroutinesApi
        @JvmStatic
        @Provides
        @RefundCartScope
        @FlowPreview
        fun provideProductMarkingResultFlow(
            cashierRefundFeatureArgs: CashierRefundFeatureArgs
        ): Flow<ProductMarkingResult> =
            cashierRefundFeatureArgs.productMarkingResultBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @RefundCartScope
        fun provideRefundCartBroadcastChannel(): RefundCartBroadcastChannel =
            RefundCartBroadcastChannel()

    }

    @Module
    interface Binders {
        @Binds
        @RefundCartScope
        fun bindCashOperationsDetailsRepository(
            cashOperationsDetailsRepositoryImpl: CashOperationsDetailsRepositoryImpl
        ): CashOperationsDetailsRepository
    }
}
