package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.creation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.ReceiptDraftRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.ReceiptDraftRepositoryImpl

@Module(includes = [ReceiptDraftCreationModule.Binders::class])
internal object ReceiptDraftCreationModule {

    @Module
    interface Binders {

        @Binds
        @ReceiptDraftCreationScope
        fun bindReceiptDraftRepository(
            impl: ReceiptDraftRepositoryImpl
        ): ReceiptDraftRepository

        @Binds
        @ReceiptDraftCreationScope
        fun bindProductRepository(
            impl: ProductRepositoryImpl
        ): ProductRepository
    }
}