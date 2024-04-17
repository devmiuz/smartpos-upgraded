package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.creation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.di.CashierSaleComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.creation.ReceiptDraftCreationFragment

@ReceiptDraftCreationScope
@Component(
    dependencies = [CashierSaleComponent::class],
    modules = [ReceiptDraftCreationModule::class]
)
internal interface ReceiptDraftCreationComponent {

    fun inject(fragment: ReceiptDraftCreationFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: CashierSaleComponent
        ): ReceiptDraftCreationComponent
    }

    companion object {

        fun create(component: CashierSaleComponent): ReceiptDraftCreationComponent =
            DaggerReceiptDraftCreationComponent
                .factory()
                .create(component)
    }
}
