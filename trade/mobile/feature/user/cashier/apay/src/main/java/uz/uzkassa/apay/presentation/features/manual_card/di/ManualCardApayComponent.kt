package uz.uzkassa.apay.presentation.features.manual_card.di

import dagger.Component
import uz.uzkassa.apay.presentation.di.CashierApayComponent
import uz.uzkassa.apay.presentation.features.manual_card.ManualCardApayFragment
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter

@ManualCardApayScope
@Component(
    dependencies = [CashierApayComponent::class],
    modules = [ManualCardApayModule::class]
)
internal abstract class ManualCardApayComponent {
    abstract val cashierApayRouter: CashierApayRouter

    abstract fun inject(manualCardApayFragment: ManualCardApayFragment)

    @Component.Factory
    interface Factory {
        fun onCreate(
            componentCashier: CashierApayComponent
        ): ManualCardApayComponent
    }

    companion object {

        fun create(componentCashier: CashierApayComponent): ManualCardApayComponent =
            DaggerManualCardApayComponent
                .factory()
                .onCreate(componentCashier)
    }

}