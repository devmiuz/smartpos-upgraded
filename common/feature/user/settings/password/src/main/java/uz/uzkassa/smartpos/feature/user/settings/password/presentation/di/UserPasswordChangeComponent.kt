package uz.uzkassa.smartpos.feature.user.settings.password.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.settings.password.dependencies.UserPasswordChangeFeatureDependencies
import uz.uzkassa.smartpos.feature.user.settings.password.presentation.UserPasswordChangeFragment

@UserPasswordChangeScope
@Component(
    dependencies = [UserPasswordChangeFeatureDependencies::class],
    modules = [UserPasswordChangeModule::class]
)
 interface UserPasswordChangeComponent {

    fun inject(fragment: UserPasswordChangeFragment)

    @Component.Factory
    interface Factory {

        fun create(
            dependencies: UserPasswordChangeFeatureDependencies
        ): UserPasswordChangeComponent
    }

    companion object {

        fun create(
            dependencies: UserPasswordChangeFeatureDependencies
        ): UserPasswordChangeComponent =
            DaggerUserPasswordChangeComponent
                .factory()
                .create(dependencies)
    }
}