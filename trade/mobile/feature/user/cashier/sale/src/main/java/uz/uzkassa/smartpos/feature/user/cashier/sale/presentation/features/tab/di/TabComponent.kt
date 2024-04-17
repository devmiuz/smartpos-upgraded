package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.tab.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.di.CashierSaleComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.tab.TabFragment

@TabScope
@Component(
    dependencies = [CashierSaleComponent::class],
    modules = [TabModule::class]
)
internal interface TabComponent {

    fun inject(fragment: TabFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: CashierSaleComponent
        ): TabComponent
    }

    companion object {

        fun create(component: CashierSaleComponent) =
            DaggerTabComponent
                .factory()
                .create(component)
    }
}