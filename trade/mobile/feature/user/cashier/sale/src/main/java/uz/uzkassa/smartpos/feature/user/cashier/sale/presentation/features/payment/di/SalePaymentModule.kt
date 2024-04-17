package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.held.ReceiptHeldBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.sale.SalePaymentBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.amount.Amount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.Discount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.SalePayment
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment.PaymentActionRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment.PaymentActionRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs

@Module(includes = [SalePaymentModule.Binders::class, SalePaymentModule.Providers::class])
internal object SalePaymentModule {

    @Module
    interface Binders {

        @Binds
        @SalePaymentScope
        fun bindPaymentActionRepository(
            impl: PaymentActionRepositoryImpl
        ): PaymentActionRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @SalePaymentScope
        @FlowPreview
        fun provideAmountFlow(
            cashierSaleFeatureArgs: CashierSaleFeatureArgs
        ): Flow<Amount> =
            cashierSaleFeatureArgs.amountBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @SalePaymentScope
        @FlowPreview
        fun provideBillFlow(
            cashierSaleFeatureArgs: CashierSaleFeatureArgs
        ): Flow<String> =
            cashierSaleFeatureArgs.billBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @SalePaymentScope
        @FlowPreview
        fun provideDiscountFlow(
            cashierSaleFeatureArgs: CashierSaleFeatureArgs
        ): Flow<Discount> =
            cashierSaleFeatureArgs.discountBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @SalePaymentScope
        @FlowPreview
        fun provideReceiptHeldFlow(
            receiptHeldBroadcastChannel: ReceiptHeldBroadcastChannel
        ): Flow<Unit> =
            receiptHeldBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @SalePaymentScope
        fun provideSalePaymentBroadcastChannel(): SalePaymentBroadcastChannel =
            SalePaymentBroadcastChannel()

        @JvmStatic
        @Provides
        @SalePaymentScope
        @FlowPreview
        fun provideSalePaymentFlow(
            salePaymentBroadcastChannel: SalePaymentBroadcastChannel
        ): Flow<SalePayment> =
            salePaymentBroadcastChannel.asFlow()

//        @JvmStatic
//        @Provides
//        @SalePaymentScope
//        fun provideSalePaymentInteractor(
//            saleInteractor: SaleInteractor,
//            salePaymentBroadcastChannel: SalePaymentBroadcastChannel
//        ): SalePaymentInteractor =
//            SalePaymentInteractor(
//                saleInteractor,
//                salePaymentBroadcastChannel
//            )
    }
}