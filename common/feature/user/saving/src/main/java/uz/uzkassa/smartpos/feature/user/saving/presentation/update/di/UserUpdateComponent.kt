package uz.uzkassa.smartpos.feature.user.saving.presentation.update.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.saving.dependencies.UserSavingFeatureDependencies
import uz.uzkassa.smartpos.feature.user.saving.presentation.update.UserUpdateFragment

@UserUpdateScope
@Component(
    dependencies = [UserSavingFeatureDependencies::class],
    modules = [UserUpdateModule::class]
)
abstract class UserUpdateComponent {

    internal abstract fun inject(fragment: UserUpdateFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: UserSavingFeatureDependencies
        ): UserUpdateComponent
    }

    internal companion object {

        fun create(
            dependencies: UserSavingFeatureDependencies
        ): UserUpdateComponent =
            DaggerUserUpdateComponent
                .factory()
                .create(dependencies)
    }
}