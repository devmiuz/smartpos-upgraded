package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.amount.CashAmountRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.amount.CashAmountRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.CashOperationsRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.CashOperationsRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.save.CashTransactionSaveRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.save.CashTransactionSaveRepositoryImpl

@Module(includes = [CashOperationsModule.Binders::class])
internal object CashOperationsModule {

    @Module
    interface Binders {

        @Binds
        @CashOperationsScope
        fun bindCashAmountRepository(
            cashAmountRepositoryImpl: CashAmountRepositoryImpl
        ): CashAmountRepository

        @Binds
        @CashOperationsScope
        fun bindCashOperationRepository(
            cashOperationRepositoryImpl: CashOperationsRepositoryImpl
        ): CashOperationsRepository

        @Binds
        @CashOperationsScope
        fun bindCashTransactionSaveRepository(
            cashTransactionSaveRepositoryImpl: CashTransactionSaveRepositoryImpl
        ): CashTransactionSaveRepository
    }

}