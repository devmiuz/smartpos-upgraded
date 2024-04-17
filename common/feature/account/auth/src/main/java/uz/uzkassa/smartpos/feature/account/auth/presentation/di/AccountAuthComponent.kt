package uz.uzkassa.smartpos.feature.account.auth.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.account.auth.dependencies.AccountAuthFeatureDependencies
import uz.uzkassa.smartpos.feature.account.auth.presentation.AccountAuthFragment

@AccountAuthScope
@Component(
    dependencies = [AccountAuthFeatureDependencies::class],
    modules = [AccountAuthModule::class]
)
abstract class AccountAuthComponent : AccountAuthFeatureDependencies {

    internal abstract fun inject(fragment: AccountAuthFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: AccountAuthFeatureDependencies
        ): AccountAuthComponent
    }

    internal companion object {

        fun create(dependencies: AccountAuthFeatureDependencies): AccountAuthComponent =
            DaggerAccountAuthComponent
                .factory()
                .create(dependencies)
    }
}