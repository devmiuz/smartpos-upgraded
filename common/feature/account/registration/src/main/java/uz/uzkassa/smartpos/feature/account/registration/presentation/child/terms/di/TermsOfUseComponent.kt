package uz.uzkassa.smartpos.feature.account.registration.presentation.child.terms.di

import dagger.Component
import uz.uzkassa.smartpos.feature.account.registration.presentation.child.terms.TermsOfUseFragment
import uz.uzkassa.smartpos.feature.account.registration.presentation.di.AccountRegistrationComponent

@TermsOfUseScope
@Component(dependencies = [AccountRegistrationComponent::class])
internal interface TermsOfUseComponent {

    fun inject(fragment: TermsOfUseFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: AccountRegistrationComponent
        ): TermsOfUseComponent
    }

    companion object {

        fun create(component: AccountRegistrationComponent): TermsOfUseComponent =
            DaggerTermsOfUseComponent
                .factory()
                .create(component)
    }
}