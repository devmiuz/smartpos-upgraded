package uz.uzkassa.smartpos.feature.account.recovery.password.presentation.child.password.di

import dagger.Component
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.child.password.NewPasswordFragment
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.di.AccountRecoveryPasswordComponent

@NewPasswordScope
@Component(dependencies = [AccountRecoveryPasswordComponent::class])
internal interface NewPasswordComponent {

    fun inject(fragment: NewPasswordFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: AccountRecoveryPasswordComponent
        ): NewPasswordComponent
    }

    companion object {

        fun create(component: AccountRecoveryPasswordComponent): NewPasswordComponent =
            DaggerNewPasswordComponent.factory()
                .create(component)
    }
}