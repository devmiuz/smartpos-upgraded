package uz.uzkassa.smartpos.feature.category.selection.setup.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.category.selection.setup.dependencies.CategorySetupFeatureDependencies
import uz.uzkassa.smartpos.feature.category.selection.setup.presentation.CategorySetupFragment

@CategorySetupScope
@Component(
    dependencies = [CategorySetupFeatureDependencies::class],
    modules = [CategorySetupModule::class]
)
abstract class CategorySetupComponent {

    internal abstract fun inject(fragment: CategorySetupFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: CategorySetupFeatureDependencies
        ): CategorySetupComponent
    }

    internal companion object {

        fun create(
            dependencies: CategorySetupFeatureDependencies
        ): CategorySetupComponent =
            DaggerCategorySetupComponent
                .factory()
                .create(dependencies)
    }
}