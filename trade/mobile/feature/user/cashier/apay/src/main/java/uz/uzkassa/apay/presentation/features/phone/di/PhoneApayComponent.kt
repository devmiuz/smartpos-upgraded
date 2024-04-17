package uz.uzkassa.apay.presentation.features.phone.di

import dagger.Component
import uz.uzkassa.apay.presentation.di.CashierApayComponent
import uz.uzkassa.apay.presentation.features.phone.PhoneApayFragment
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter

@PhoneApayScope
@Component(
    modules = [PhoneApayModule::class],
    dependencies = [CashierApayComponent::class]
)
internal interface PhoneApayComponent {

    fun inject(fragment: PhoneApayFragment)

    val cashierApayRouter: CashierApayRouter

    @Component.Factory
    interface Factory {
        fun onCreate(
            componentCashier: CashierApayComponent
        ): PhoneApayComponent
    }

    companion object {

        fun create(componentCashier: CashierApayComponent): PhoneApayComponent =
            DaggerPhoneApayComponent
                .factory()
                .onCreate(componentCashier)
    }
}