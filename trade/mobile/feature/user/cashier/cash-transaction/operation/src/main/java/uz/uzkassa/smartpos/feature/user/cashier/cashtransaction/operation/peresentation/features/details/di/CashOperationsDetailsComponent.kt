package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.details.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.di.CashOperationsComponent
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.details.CashOperationsDetailsFragment

@CashOperationsDetailsScope
@Component(
    dependencies = [CashOperationsComponent::class],
    modules = [CashOperationsDetailsModule::class]
)
abstract class CashOperationsDetailsComponent {

    internal abstract fun inject(fragment: CashOperationsDetailsFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            component: CashOperationsComponent
        ): CashOperationsDetailsComponent
    }

    internal companion object {
        fun create(
            component: CashOperationsComponent
        ): CashOperationsDetailsComponent =
            DaggerCashOperationsDetailsComponent
                .factory()
                .create(component)
    }
}