package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.list.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.di.CashierSaleComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.list.ReceiptDraftListFragment

@ReceiptDraftListScope
@Component(
    dependencies = [CashierSaleComponent::class],
    modules = [ReceiptDraftListModule::class]
)
internal interface ReceiptDraftListComponent {

    fun inject(fragment: ReceiptDraftListFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: CashierSaleComponent
        ): ReceiptDraftListComponent
    }

    companion object {

        fun create(component: CashierSaleComponent): ReceiptDraftListComponent =
            DaggerReceiptDraftListComponent
                .factory()
                .create(component)
    }
}