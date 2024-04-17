package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.process.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.di.SalePaymentComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.process.SaleProcessFragment

@SaleProcessScope
@Component(dependencies = [SalePaymentComponent::class])
internal interface SaleProcessComponent {

    fun inject(fragment: SaleProcessFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: SalePaymentComponent
        ): SaleProcessComponent
    }

    companion object {

        fun create(component: SalePaymentComponent): SaleProcessComponent =
            DaggerSaleProcessComponent
                .factory()
                .create(component)
    }
}