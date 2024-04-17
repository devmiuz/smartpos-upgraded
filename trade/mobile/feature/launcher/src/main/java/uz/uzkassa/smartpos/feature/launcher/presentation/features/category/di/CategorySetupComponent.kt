package uz.uzkassa.smartpos.feature.launcher.presentation.features.category.di

import dagger.Component
import uz.uzkassa.smartpos.feature.launcher.presentation.di.LauncherComponent
import uz.uzkassa.smartpos.feature.launcher.presentation.features.category.CategorySetupFragment

@CategorySetupScope
@Component(
    dependencies = [LauncherComponent::class],
    modules = [CategorySetupModule::class]
)
internal interface CategorySetupComponent {

    fun inject(fragment: CategorySetupFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: LauncherComponent
        ): CategorySetupComponent
    }

    companion object {

        fun create(component: LauncherComponent): CategorySetupComponent =
            DaggerCategorySetupComponent
                .factory()
                .create(component)
    }
}