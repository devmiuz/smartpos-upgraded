package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.provider.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment.providers.PaymentProvidersRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment.providers.PaymentProvidersImpl

@Module(includes = [PaymentProvidersModule.Binders::class])
internal object PaymentProvidersModule {

    @Module
    interface Binders {

        @Binds
        @PaymentProvidersScope
        fun bindProvidersRepository(
            impl: PaymentProvidersImpl
        ): PaymentProvidersRepository
    }
}