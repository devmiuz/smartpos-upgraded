package uz.uzkassa.common.feature.category.selection.presentation.di

import dagger.Component
import uz.uzkassa.common.feature.category.selection.dependencies.CategorySelectionFeatureDependencies
import uz.uzkassa.common.feature.category.selection.presentation.CategorySelectionFragment

@CategorySelectionScope
@Component(
    dependencies = [CategorySelectionFeatureDependencies::class],
    modules = [CategorySelectionModule::class]
)
abstract class CategorySelectionComponent : CategorySelectionFeatureDependencies {

    internal abstract fun inject(fragment: CategorySelectionFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: CategorySelectionFeatureDependencies
        ): CategorySelectionComponent
    }

    internal companion object {

        fun create(
            dependencies: CategorySelectionFeatureDependencies
        ): CategorySelectionComponent =
            DaggerCategorySelectionComponent
                .factory()
                .create(dependencies)
    }
}