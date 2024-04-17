package uz.uzkassa.smartpos.feature.user.settings.language.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.settings.language.dependencies.UserLanguageChangeFeatureDependencies
import uz.uzkassa.smartpos.feature.user.settings.language.presentation.UserLanguageChangeFragment

@UserLanguageChangeScope
@Component(
    dependencies = [UserLanguageChangeFeatureDependencies::class],
    modules = [UserLanguageChangeModule::class]
)
 interface UserLanguageChangeComponent {

    fun inject(fragment: UserLanguageChangeFragment)

    @Component.Factory
    interface Factory {
        fun create(
            dependencies: UserLanguageChangeFeatureDependencies
        ): UserLanguageChangeComponent
    }

    companion object {

        fun create(
            dependencies: UserLanguageChangeFeatureDependencies
        ): UserLanguageChangeComponent =
            DaggerUserLanguageChangeComponent
                .factory()
                .create(dependencies)
    }
}