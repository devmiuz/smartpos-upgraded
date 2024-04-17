package uz.uzkassa.smartpos.feature.receipt.remote.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.receipt.remote.data.repository.receipt.remote.ReceiptRemoteRepository
import uz.uzkassa.smartpos.feature.receipt.remote.data.repository.receipt.remote.ReceiptRemoteRepositoryImpl

@Module(includes = [ReceiptRemoteModule.Binders::class])
internal object ReceiptRemoteModule {

    @Module
    interface Binders {

        @Binds
        @ReceiptRemoteScope
        fun bindReceiptRemoteRepository(impl: ReceiptRemoteRepositoryImpl): ReceiptRemoteRepository
    }
}