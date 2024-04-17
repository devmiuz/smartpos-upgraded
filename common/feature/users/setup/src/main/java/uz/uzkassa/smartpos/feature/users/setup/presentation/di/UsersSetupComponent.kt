package uz.uzkassa.smartpos.feature.users.setup.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.users.setup.dependencies.UsersSetupFeatureDependencies
import uz.uzkassa.smartpos.feature.users.setup.presentation.UsersSetupFragment

@UsersSetupScope
@Component(
    dependencies = [UsersSetupFeatureDependencies::class],
    modules = [UsersSetupModule::class]
)
abstract class UsersSetupComponent {

    internal abstract fun inject(fragment: UsersSetupFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: UsersSetupFeatureDependencies
        ): UsersSetupComponent
    }

    internal companion object {

        fun create(
            dependencies: UsersSetupFeatureDependencies
        ): UsersSetupComponent =
            DaggerUsersSetupComponent
                .factory()
                .create(dependencies)
    }
}