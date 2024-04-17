package uz.uzkassa.smartpos.feature.category.list.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.category.list.dependencies.CategoryListFeatureDependencies
import uz.uzkassa.smartpos.feature.category.list.presentation.CategoryListFragment

@CategoryListScope
@Component(
    dependencies = [CategoryListFeatureDependencies::class],
    modules = [CategoryListModule::class]
)
abstract class CategoryListComponent : CategoryListFeatureDependencies {

    internal abstract fun inject(fragment: CategoryListFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: CategoryListFeatureDependencies
        ): CategoryListComponent
    }

    internal companion object {

        fun create(
            dependencies: CategoryListFeatureDependencies
        ): CategoryListComponent =
            DaggerCategoryListComponent
                .factory()
                .create(dependencies)
    }
}