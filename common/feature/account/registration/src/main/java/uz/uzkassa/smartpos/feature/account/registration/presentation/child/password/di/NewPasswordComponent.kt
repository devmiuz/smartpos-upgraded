package uz.uzkassa.smartpos.feature.account.registration.presentation.child.password.di

import dagger.Component
import uz.uzkassa.smartpos.feature.account.registration.presentation.child.password.NewPasswordFragment
import uz.uzkassa.smartpos.feature.account.registration.presentation.di.AccountRegistrationComponent

@NewPasswordScope
@Component(dependencies = [AccountRegistrationComponent::class])
internal interface NewPasswordComponent {

    fun inject(fragment: NewPasswordFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: AccountRegistrationComponent
        ): NewPasswordComponent
    }

    companion object {

        fun create(component: AccountRegistrationComponent): NewPasswordComponent =
            DaggerNewPasswordComponent
                .factory()
                .create(component)
    }
}