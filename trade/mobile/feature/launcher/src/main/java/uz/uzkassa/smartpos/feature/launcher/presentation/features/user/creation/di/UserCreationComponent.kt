package uz.uzkassa.smartpos.feature.launcher.presentation.features.user.creation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.launcher.presentation.di.LauncherComponent
import uz.uzkassa.smartpos.feature.launcher.presentation.features.user.creation.UserCreationFragment

@UserCreationScope
@Component(
    dependencies = [LauncherComponent::class],
    modules = [UserCreationModule::class]
)
internal interface UserCreationComponent {

    fun inject(fragment: UserCreationFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: LauncherComponent
        ): UserCreationComponent
    }

    companion object {

        fun create(component: LauncherComponent): UserCreationComponent =
            DaggerUserCreationComponent
                .factory()
                .create(component)
    }
}