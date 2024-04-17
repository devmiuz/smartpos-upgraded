package uz.uzkassa.smartpos.feature.user.auth.presentation.features.cashier.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.auth.data.repository.cashier.CashierAuthRepository
import uz.uzkassa.smartpos.feature.user.auth.data.repository.cashier.CashierAuthRepositoryImpl

@Module(includes = [CashierAuthModule.Binders::class])
internal object CashierAuthModule {

    @Module
    interface Binders {

        @Binds
        @CashierAuthScope
        fun bindCashierAuthRepository(
            impl: CashierAuthRepositoryImpl
        ): CashierAuthRepository
    }
}