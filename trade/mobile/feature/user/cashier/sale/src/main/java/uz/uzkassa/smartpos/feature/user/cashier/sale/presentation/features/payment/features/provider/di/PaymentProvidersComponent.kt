package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.provider.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.di.SalePaymentComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.provider.PaymentProvidersFragment

@PaymentProvidersScope
@Component(
    dependencies = [SalePaymentComponent::class],
    modules = [PaymentProvidersModule::class]
)
internal interface PaymentProvidersComponent {

    fun inject(fragment: PaymentProvidersFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: SalePaymentComponent
        ): PaymentProvidersComponent
    }

    companion object {

        fun create(component: SalePaymentComponent): PaymentProvidersComponent =
            DaggerPaymentProvidersComponent
                .factory()
                .create(component)
    }
}