package uz.uzkassa.smartpos.feature.user.settings.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.settings.dependencies.UserSettingsFeatureDependencies
import uz.uzkassa.smartpos.feature.user.settings.presentation.UserSettingsFragment

@UserSettingsScope
@Component(
    dependencies = [UserSettingsFeatureDependencies::class],
    modules = [UserSettingsModule::class]
)
abstract class UserSettingsComponent {

    internal abstract fun inject(fragment: UserSettingsFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: UserSettingsFeatureDependencies
        ): UserSettingsComponent
    }

    internal companion object {

        fun create(
            dependencies: UserSettingsFeatureDependencies
        ): UserSettingsComponent =
            DaggerUserSettingsComponent
                .factory()
                .create(dependencies)
    }
}
