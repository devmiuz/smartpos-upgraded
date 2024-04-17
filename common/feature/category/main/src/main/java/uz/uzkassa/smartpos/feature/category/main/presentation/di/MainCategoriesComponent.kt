package uz.uzkassa.smartpos.feature.category.main.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.category.main.dependencies.MainCategoriesFeatureDependencies
import uz.uzkassa.smartpos.feature.category.main.presentation.MainCategoriesFragment

@MainCategoriesScope
@Component(
    dependencies = [MainCategoriesFeatureDependencies::class],
    modules = [MainCategoriesModule::class]
)
abstract class MainCategoriesComponent {

    internal abstract fun inject(fragment: MainCategoriesFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: MainCategoriesFeatureDependencies
        ): MainCategoriesComponent
    }

    internal companion object {

        fun create(
            mainCategoriesDependencies: MainCategoriesFeatureDependencies
        ): MainCategoriesComponent =
            DaggerMainCategoriesComponent
                .factory()
                .create(mainCategoriesDependencies)
    }
}