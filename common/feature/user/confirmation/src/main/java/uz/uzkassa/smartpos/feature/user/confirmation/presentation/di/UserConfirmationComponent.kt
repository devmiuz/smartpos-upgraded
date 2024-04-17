package uz.uzkassa.smartpos.feature.user.confirmation.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.confirmation.dependencies.UserConfirmationFeatureDependencies
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.UserConfirmationFragment
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.navigation.UserConfirmationRouter

@UserConfirmationScope
@Component(
    dependencies = [UserConfirmationFeatureDependencies::class],
    modules = [UserConfirmationModuleNavigation::class]
)
abstract class UserConfirmationComponent : UserConfirmationFeatureDependencies {

    internal abstract val userConfirmationRouter: UserConfirmationRouter

    internal abstract fun inject(fragment: UserConfirmationFragment)

    @Component.Factory
    internal interface Factory {

        fun create(
            dependencies: UserConfirmationFeatureDependencies
        ): UserConfirmationComponent
    }

    internal companion object {

        fun create(
            dependencies: UserConfirmationFeatureDependencies
        ): UserConfirmationComponent =
            DaggerUserConfirmationComponent
                .factory()
                .create(dependencies)
    }
}