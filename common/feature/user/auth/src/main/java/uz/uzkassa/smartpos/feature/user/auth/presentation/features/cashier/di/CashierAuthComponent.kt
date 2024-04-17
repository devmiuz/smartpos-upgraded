package uz.uzkassa.smartpos.feature.user.auth.presentation.features.cashier.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.auth.presentation.di.UserAuthComponent
import uz.uzkassa.smartpos.feature.user.auth.presentation.features.cashier.CashierAuthFragment

@CashierAuthScope
@Component(dependencies = [UserAuthComponent::class], modules = [CashierAuthModule::class])
internal interface CashierAuthComponent {

    fun inject(fragment: CashierAuthFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: UserAuthComponent
        ): CashierAuthComponent
    }

    companion object {

        fun create(component: UserAuthComponent): CashierAuthComponent =
            DaggerCashierAuthComponent
                .factory()
                .create(component)
    }
}