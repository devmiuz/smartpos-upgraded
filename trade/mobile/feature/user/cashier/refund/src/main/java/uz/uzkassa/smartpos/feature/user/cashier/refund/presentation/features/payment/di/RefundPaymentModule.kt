package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.confirmation.SupervisorConfirmationBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.payment.RefundPaymentBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.Amount
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.confirmation.SupervisorConfirmationState
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.RefundPayment
import uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies.CashierRefundFeatureArgs

@Module(includes = [RefundPaymentModule.Providers::class])
internal object RefundPaymentModule {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @RefundPaymentScope
        @FlowPreview
        fun provideAmountFlow(
            cashierRefundFeatureArgs: CashierRefundFeatureArgs
        ): Flow<Amount> =
            cashierRefundFeatureArgs.amountBroadcastChannel.asFlow()


        @JvmStatic
        @Provides
        @RefundPaymentScope
        fun provideRefundPaymentBroadcastChannel(): RefundPaymentBroadcastChannel =
            RefundPaymentBroadcastChannel()

        @JvmStatic
        @Provides
        @RefundPaymentScope
        @FlowPreview
        fun provideRefundPaymentFlow(
            refundPaymentBroadcastChannel: RefundPaymentBroadcastChannel
        ): Flow<RefundPayment> =
            refundPaymentBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @RefundPaymentScope
        @FlowPreview
        fun provideSupervisorConfirmationChannel(
        ): SupervisorConfirmationBroadcastChannel =
            SupervisorConfirmationBroadcastChannel()

        @JvmStatic
        @Provides
        @RefundPaymentScope
        @FlowPreview
        fun provideSupervisorConfirmationFlow(
            cashierRefundFeatureArgs: CashierRefundFeatureArgs
        ): Flow<SupervisorConfirmationState> =
            cashierRefundFeatureArgs.supervisorConfirmationBroadcastChannel.asFlow()
    }
}