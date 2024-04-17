package uz.uzkassa.apay.presentation.features.card.di

import dagger.Component
import uz.uzkassa.apay.presentation.di.CashierApayComponent
import uz.uzkassa.apay.presentation.features.card.CardApayFragment
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter

@CardApayScope
@Component(
    dependencies = [CashierApayComponent::class],
    modules = [CardApayModule::class]
)
internal abstract class CardApayComponent {

    abstract val cashierApayRouter: CashierApayRouter

    abstract fun inject(cardApayFragment: CardApayFragment)

    @Component.Factory
    interface Factory {
        fun onCreate(
            componentCashier: CashierApayComponent
        ): CardApayComponent
    }

    companion object {

        fun create(componentCashier: CashierApayComponent): CardApayComponent =
            DaggerCardApayComponent
                .factory()
                .onCreate(componentCashier)
    }

}