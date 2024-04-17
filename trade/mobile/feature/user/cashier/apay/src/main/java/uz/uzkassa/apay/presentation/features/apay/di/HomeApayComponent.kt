package uz.uzkassa.apay.presentation.features.apay.di

import dagger.Component
import uz.uzkassa.apay.presentation.di.CashierApayComponent
import uz.uzkassa.apay.presentation.features.apay.HomeApayFragment
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter

@HomeApayScope
@Component(
    dependencies = [CashierApayComponent::class],
    modules = [HomeApayModule::class]
)
internal abstract class HomeApayComponent {

    abstract val cashierApayRouter: CashierApayRouter

    abstract fun inject(homeApayFragment: HomeApayFragment)

    @Component.Factory
    interface Factory {
        fun onCreate(
            componentCashier: CashierApayComponent
        ): HomeApayComponent
    }

    companion object {

        fun create(componentCashier: CashierApayComponent): HomeApayComponent =
            DaggerHomeApayComponent
                .factory()
                .onCreate(componentCashier)
    }
}