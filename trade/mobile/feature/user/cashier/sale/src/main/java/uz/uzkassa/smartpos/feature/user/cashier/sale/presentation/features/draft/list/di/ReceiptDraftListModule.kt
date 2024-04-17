package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.list.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.held.ReceiptHeldBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.ReceiptDraftRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.ReceiptDraftRepositoryImpl

@Module(
    includes = [
        ReceiptDraftListModule.Binders::class,
        ReceiptDraftListModule.Providers::class
    ]
)
internal object ReceiptDraftListModule {

    @Module
    interface Binders {

        @Binds
        @ReceiptDraftListScope
        fun bindReceiptDraftRepository(
            impl: ReceiptDraftRepositoryImpl
        ): ReceiptDraftRepository

        @Binds
        @ReceiptDraftListScope
        fun bindProductRepository(
            impl: ProductRepositoryImpl
        ): ProductRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @ReceiptDraftListScope
        @FlowPreview
        fun provideReceiptHeldFlow(
            receiptHeldBroadcastChannel: ReceiptHeldBroadcastChannel
        ): Flow<Unit> =
            receiptHeldBroadcastChannel.asFlow()
    }
}