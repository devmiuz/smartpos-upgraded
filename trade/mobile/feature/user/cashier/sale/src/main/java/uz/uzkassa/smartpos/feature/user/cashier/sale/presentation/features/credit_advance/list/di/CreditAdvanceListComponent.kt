package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.credit_advance.CreditAdvanceBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.held.ReceiptHeldBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.receipt.credit_advance.list.CreditAdvanceListInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.di.CashierSaleComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.CreditAdvanceListFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter

@CreditAdvanceListScope
@Component(
    dependencies = [CashierSaleComponent::class],
    modules = [CreditAdvanceListModule::class]
)
internal interface CreditAdvanceListComponent {

    fun inject(fragment: CreditAdvanceListFragment)

    val cashierSaleRouter: CashierSaleRouter

    val creditAdvanceListInteractor: CreditAdvanceListInteractor

    val creditAdvanceBroadcastChannel: CreditAdvanceBroadcastChannel


    @Component.Factory
    interface Factory {
        fun create(
            component: CashierSaleComponent
        ): CreditAdvanceListComponent
    }

    companion object {
        fun create(component: CashierSaleComponent): CreditAdvanceListComponent =
            DaggerCreditAdvanceListComponent
                .factory()
                .create(component)
    }

}