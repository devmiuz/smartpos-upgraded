package uz.uzkassa.smartpos.feature.account.recovery.password.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.account.recovery.password.dependencies.AccountRecoveryPasswordFeatureDependencies
import uz.uzkassa.smartpos.feature.account.recovery.password.domain.RecoveryPasswordInteractor
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.AccountRecoveryPasswordFragment
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.navigation.RecoveryPasswordRouter

@AccountRecoveryPasswordScope
@Component(
    dependencies = [AccountRecoveryPasswordFeatureDependencies::class],
    modules = [AccountRecoveryPasswordModule::class, AccountRecoveryPasswordModuleNavigation::class]
)
abstract class AccountRecoveryPasswordComponent : AccountRecoveryPasswordFeatureDependencies {

    internal abstract val recoveryPasswordInteractor: RecoveryPasswordInteractor

    internal abstract val recoveryPasswordRouter: RecoveryPasswordRouter

    internal abstract fun inject(fragment: AccountRecoveryPasswordFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: AccountRecoveryPasswordFeatureDependencies
        ): AccountRecoveryPasswordComponent
    }

    internal companion object {

        fun create(
            dependencies: AccountRecoveryPasswordFeatureDependencies
        ): AccountRecoveryPasswordComponent =
            DaggerAccountRecoveryPasswordComponent
                .factory()
                .create(dependencies)
    }
}