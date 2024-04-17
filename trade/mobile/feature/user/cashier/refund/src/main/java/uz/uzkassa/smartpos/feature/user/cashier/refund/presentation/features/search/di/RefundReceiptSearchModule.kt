package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.ReceiptRepository
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.ReceiptRepositoryImpl

@Module(includes = [RefundReceiptSearchModule.Binders::class])
internal object RefundReceiptSearchModule {

    @Module
    interface Binders {

        @Binds
        @RefundReceiptSearchScope
        fun bindReceiptRepository(impl: ReceiptRepositoryImpl): ReceiptRepository
    }
}