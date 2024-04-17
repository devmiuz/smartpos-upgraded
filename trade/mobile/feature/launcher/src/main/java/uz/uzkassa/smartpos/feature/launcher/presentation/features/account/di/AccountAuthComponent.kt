package uz.uzkassa.smartpos.feature.launcher.presentation.features.account.di

import dagger.Component
import uz.uzkassa.smartpos.feature.launcher.presentation.di.LauncherComponent
import uz.uzkassa.smartpos.feature.launcher.presentation.features.account.AccountAuthFragment

@AccountAuthScope
@Component(dependencies = [LauncherComponent::class])
internal interface AccountAuthComponent {

    fun inject(fragment: AccountAuthFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: LauncherComponent
        ): AccountAuthComponent
    }

    companion object {

        fun create(component: LauncherComponent): AccountAuthComponent =
            DaggerAccountAuthComponent
                .factory()
                .create(component)
    }
}