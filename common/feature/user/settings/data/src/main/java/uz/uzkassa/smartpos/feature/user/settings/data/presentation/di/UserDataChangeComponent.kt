package uz.uzkassa.smartpos.feature.user.settings.data.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.settings.data.dependencies.UserDataChangeFeatureDependencies
import uz.uzkassa.smartpos.feature.user.settings.data.presentation.UserDataChangeFragment

@UserDataChangeScope
@Component(
    dependencies = [UserDataChangeFeatureDependencies::class],
    modules = [UserDataChangeModule::class]
)
 interface UserDataChangeComponent {

    fun inject(fragment: UserDataChangeFragment)

    @Component.Factory
    interface Factory {
        fun create(
            dependencies: UserDataChangeFeatureDependencies
        ): UserDataChangeComponent
    }

    companion object {

        fun create(
            dependencies: UserDataChangeFeatureDependencies
        ): UserDataChangeComponent =
            DaggerUserDataChangeComponent
                .factory()
                .create(dependencies)
    }
}