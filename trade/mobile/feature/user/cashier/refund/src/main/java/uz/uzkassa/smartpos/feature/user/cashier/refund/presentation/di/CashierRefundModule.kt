package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.confirmation.SupervisorConfirmationBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.product.marking.ProductMarkingRepository
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.product.marking.ProductMarkingRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.save.ReceiptSaveRepository
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.save.ReceiptSaveRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.RefundInteractor
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.product.marking.ProductMarkingInteractor

@Module(
    includes = [
        CashierRefundModule.Binders::class,
        CashierRefundModule.Providers::class
    ]
)
internal object CashierRefundModule {

    @Module
    interface Binders {

        @Binds
        @CashierRefundScope
        fun bindReceiptSaveRepository(
            receiptSaveRepositoryImpl: ReceiptSaveRepositoryImpl
        ): ReceiptSaveRepository

        @Binds
        @CashierRefundScope
        fun bindProductMarkingRepository(
            impl: ProductMarkingRepositoryImpl
        ): ProductMarkingRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @CashierRefundScope
        fun provideSupervisorConfirmationStateFlow(): SupervisorConfirmationBroadcastChannel =
            SupervisorConfirmationBroadcastChannel()

        @JvmStatic
        @Provides
        @CashierRefundScope
        fun provideRefundInteractor(): RefundInteractor =
            RefundInteractor()

        @JvmStatic
        @Provides
        @CashierRefundScope
        fun provideProductMarkingInteractor(
            coroutineContextManager: CoroutineContextManager,
            productMarkingRepository: ProductMarkingRepository
        ): ProductMarkingInteractor =
            ProductMarkingInteractor(
                coroutineContextManager,
                productMarkingRepository
            )

    }
}