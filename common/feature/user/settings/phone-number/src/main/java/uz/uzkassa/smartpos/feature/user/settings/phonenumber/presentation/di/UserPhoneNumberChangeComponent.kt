package uz.uzkassa.smartpos.feature.user.settings.phonenumber.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.dependencies.UserPhoneNumberChangeFeatureDependencies
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.presentation.UserPhoneNumberChangeFragment

@UserPhoneNumberChangeScope
@Component(
    dependencies = [UserPhoneNumberChangeFeatureDependencies::class],
    modules = [UserPhoneNumberChangeModule::class]
)
 interface UserPhoneNumberChangeComponent {

    fun inject(fragment: UserPhoneNumberChangeFragment)

    @Component.Factory
    interface Factory {
        fun create(
            dependencies: UserPhoneNumberChangeFeatureDependencies
        ): UserPhoneNumberChangeComponent
    }

    companion object {

        fun create(
            dependencies: UserPhoneNumberChangeFeatureDependencies
        ): UserPhoneNumberChangeComponent =
            DaggerUserPhoneNumberChangeComponent
                .factory()
                .create(dependencies)
    }
}
