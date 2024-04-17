package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.credit_advance.CreditAdvanceBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.held.ReceiptHeldBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.credit_advance.CreditAdvanceRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.credit_advance.CreditAdvanceRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.di.CashierSaleScope
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.creation.di.ReceiptDraftCreationScope

@Module(
    includes = [
        CreditAdvanceListModule.Binders::class,
        CreditAdvanceListModule.Providers::class
    ]
)
internal object CreditAdvanceListModule {

    @Module
    interface Binders {
        @Binds
        @CreditAdvanceListScope
        fun bindCreditAdvanceRepository(
            impl: CreditAdvanceRepositoryImpl
        ): CreditAdvanceRepository

        @Binds
        @CreditAdvanceListScope
        fun bindProductRepository(
            impl: ProductRepositoryImpl
        ): ProductRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @CreditAdvanceListScope
        fun provideCreditAdvanceBroadcastChannel(): CreditAdvanceBroadcastChannel =
            CreditAdvanceBroadcastChannel()

        @JvmStatic
        @Provides
        @CreditAdvanceListScope
        @FlowPreview
        fun provideCreditAdvanceFlow(
            creditAdvanceBroadcastChannel: CreditAdvanceBroadcastChannel
        ): Flow<String> =
            creditAdvanceBroadcastChannel.asFlow()
    }
}