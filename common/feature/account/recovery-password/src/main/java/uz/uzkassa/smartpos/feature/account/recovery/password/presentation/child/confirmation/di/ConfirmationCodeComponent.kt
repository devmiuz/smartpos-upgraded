package uz.uzkassa.smartpos.feature.account.recovery.password.presentation.child.confirmation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.child.confirmation.ConfirmationCodeFragment
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.di.AccountRecoveryPasswordComponent

@ConfirmationCodeScope
@Component(dependencies = [AccountRecoveryPasswordComponent::class])
internal interface ConfirmationCodeComponent {

    fun inject(fragment: ConfirmationCodeFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: AccountRecoveryPasswordComponent
        ): ConfirmationCodeComponent
    }

    companion object {

        fun create(component: AccountRecoveryPasswordComponent): ConfirmationCodeComponent =
            DaggerConfirmationCodeComponent
                .factory()
                .create(component)
    }
}