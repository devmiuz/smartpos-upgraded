package uz.uzkassa.smartpos.feature.user.list.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.list.dependencies.UserListFeatureDependencies
import uz.uzkassa.smartpos.feature.user.list.presentation.UserListFragment

@UserListScope
@Component(
    dependencies = [UserListFeatureDependencies::class],
    modules = [UserListModule::class]
)
abstract class UserListComponent {

    internal abstract fun inject(fragment: UserListFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: UserListFeatureDependencies
        ): UserListComponent
    }

    internal companion object {

        fun create(
            dependencies: UserListFeatureDependencies
        ): UserListComponent =
            DaggerUserListComponent
                .factory()
                .create(dependencies)
    }
}