package uz.uzkassa.smartpos.feature.launcher.presentation.features.language.di

import dagger.Component
import uz.uzkassa.smartpos.feature.launcher.presentation.di.LauncherComponent
import uz.uzkassa.smartpos.feature.launcher.presentation.features.language.LanguageSelectionFragment

@LanguageSelectionScope
@Component(
    dependencies = [LauncherComponent::class],
    modules = [LanguageSelectionModule::class]
)
internal interface LanguageSelectionComponent {

    fun inject(fragment: LanguageSelectionFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: LauncherComponent
        ): LanguageSelectionComponent
    }

    companion object {

        fun create(component: LauncherComponent): LanguageSelectionComponent =
            DaggerLanguageSelectionComponent
                .factory()
                .create(component)
    }
}