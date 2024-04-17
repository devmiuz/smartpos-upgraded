package uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth.di

import dagger.Component
import uz.uzkassa.smartpos.feature.launcher.presentation.di.LauncherComponent
import uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth.UserAuthFragment

@UserAuthScope
@Component(
    dependencies = [LauncherComponent::class],
    modules = [UserAuthModule::class]
)
internal interface UserAuthComponent {

    fun inject(fragment: UserAuthFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: LauncherComponent
        ): UserAuthComponent
    }

    companion object {

        fun create(component: LauncherComponent): UserAuthComponent =
            DaggerUserAuthComponent
                .factory()
                .create(component)
    }
}