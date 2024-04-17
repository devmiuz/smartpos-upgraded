package uz.uzkassa.smartpos.feature.account.registration.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.account.registration.dependencies.AccountRegistrationFeatureDependencies
import uz.uzkassa.smartpos.feature.account.registration.domain.RegistrationInteractor
import uz.uzkassa.smartpos.feature.account.registration.presentation.AccountRegistrationFragment
import uz.uzkassa.smartpos.feature.account.registration.presentation.navigation.RegistrationRouter

@AccountRegistrationScope
@Component(
    dependencies = [AccountRegistrationFeatureDependencies::class],
    modules = [AccountRegistrationModule::class, AccountRegistrationModuleNavigation::class]
)
abstract class AccountRegistrationComponent : AccountRegistrationFeatureDependencies {

    internal abstract val registrationInteractor: RegistrationInteractor

    internal abstract val registrationRouter: RegistrationRouter

    internal abstract fun inject(fragment: AccountRegistrationFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: AccountRegistrationFeatureDependencies
        ): AccountRegistrationComponent
    }

    internal companion object {

        fun create(
            dependencies: AccountRegistrationFeatureDependencies
        ): AccountRegistrationComponent =
            DaggerAccountRegistrationComponent
                .factory()
                .create(dependencies)
    }
}