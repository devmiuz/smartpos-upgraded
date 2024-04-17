package uz.uzkassa.smartpos.feature.category.saving.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.category.saving.dependencies.CategorySavingFeatureDependencies
import uz.uzkassa.smartpos.feature.category.saving.presentation.CategoryCreationFragment

@CategoryCreationScope
@Component(
    dependencies = [CategorySavingFeatureDependencies::class],
    modules = [CategoryCreationModule::class]
)
abstract class CategoryCreationComponent {

    internal abstract fun inject(fragment: CategoryCreationFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: CategorySavingFeatureDependencies
        ): CategoryCreationComponent
    }

    internal companion object {

        fun create(
            dependencies: CategorySavingFeatureDependencies
        ): CategoryCreationComponent =
            DaggerCategoryCreationComponent
                .factory()
                .create(dependencies)
    }
}