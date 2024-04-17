package uz.uzkassa.smartpos.feature.account.registration.presentation.child.confirmation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.account.registration.presentation.child.confirmation.ConfirmationCodeFragment
import uz.uzkassa.smartpos.feature.account.registration.presentation.di.AccountRegistrationComponent

@ConfirmationCodeScope
@Component(dependencies = [AccountRegistrationComponent::class])
internal interface ConfirmationCodeComponent {

    fun inject(fragment: ConfirmationCodeFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: AccountRegistrationComponent
        ): ConfirmationCodeComponent
    }

    companion object {

        fun create(component: AccountRegistrationComponent): ConfirmationCodeComponent =
            DaggerConfirmationCodeComponent
                .factory()
                .create(component)
    }
}