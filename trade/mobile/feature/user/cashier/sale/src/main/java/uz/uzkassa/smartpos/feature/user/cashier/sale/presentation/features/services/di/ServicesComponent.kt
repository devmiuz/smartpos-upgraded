package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.services.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.di.CashierSaleComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.services.ServicesFragment

@ServicesScope
@Component(
    dependencies = [CashierSaleComponent::class],
    modules = [ServicesModule::class]
)
internal interface ServicesComponent {

    fun inject(fragment: ServicesFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: CashierSaleComponent
        ): ServicesComponent
    }

    companion object {

        fun create(component: CashierSaleComponent): ServicesComponent =
            DaggerServicesComponent
                .factory()
                .create(component)
    }
}