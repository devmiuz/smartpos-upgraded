package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.details.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.details.CashOperationsDetailsRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.details.CashOperationsDetailsRepositoryImpl

@Module(includes = [CashOperationsDetailsModule.Binders::class])
internal object CashOperationsDetailsModule {

    @Module
    interface Binders {

        @Binds
        @CashOperationsDetailsScope
        fun bindCashOperationsDetailsRepository(
            cashOperationsDetailsRepositoryImpl: CashOperationsDetailsRepositoryImpl
        ): CashOperationsDetailsRepository
    }
}