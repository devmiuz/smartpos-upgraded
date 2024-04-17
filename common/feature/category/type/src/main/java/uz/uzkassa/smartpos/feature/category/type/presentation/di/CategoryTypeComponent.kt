package uz.uzkassa.smartpos.feature.category.type.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.category.type.dependencies.CategoryTypeFeatureDependencies
import uz.uzkassa.smartpos.feature.category.type.presentation.CategoryTypeFragment

@CategoryTypeScope
@Component(
    dependencies = [CategoryTypeFeatureDependencies::class],
    modules = [CategoryTypeModule::class]
)
abstract class CategoryTypeComponent {

    internal abstract fun inject(fragment: CategoryTypeFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: CategoryTypeFeatureDependencies
        ): CategoryTypeComponent
    }

    internal companion object {

        fun create(
            dependencies: CategoryTypeFeatureDependencies
        ): CategoryTypeComponent =
            DaggerCategoryTypeComponent
                .factory()
                .create(dependencies)
    }
}