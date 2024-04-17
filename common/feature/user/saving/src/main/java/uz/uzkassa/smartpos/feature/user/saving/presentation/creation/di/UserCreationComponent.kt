package uz.uzkassa.smartpos.feature.user.saving.presentation.creation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.saving.dependencies.UserSavingFeatureDependencies
import uz.uzkassa.smartpos.feature.user.saving.presentation.creation.UserCreationFragment

@UserCreationScope
@Component(
    dependencies = [UserSavingFeatureDependencies::class],
    modules = [UserCreationModule::class]
)
abstract class UserCreationComponent {

    internal abstract fun inject(fragment: UserCreationFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: UserSavingFeatureDependencies
        ): UserCreationComponent
    }

    internal companion object {

        fun create(
            dependencies: UserSavingFeatureDependencies
        ): UserCreationComponent =
            DaggerUserCreationComponent
                .factory()
                .create(dependencies)
    }
}
