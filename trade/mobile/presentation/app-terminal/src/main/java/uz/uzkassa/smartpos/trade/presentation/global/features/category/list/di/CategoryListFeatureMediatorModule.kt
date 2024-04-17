package uz.uzkassa.smartpos.trade.presentation.global.features.category.list.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.category.list.dependencies.CategoryListFeatureArgs
import uz.uzkassa.smartpos.feature.category.list.dependencies.CategoryListFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.category.list.CategoryListFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.category.list.runner.CategoryListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.category.saving.runner.CategorySavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.product.list.runner.ProductListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        CategoryListFeatureMediatorModule.Binders::class,
        CategoryListFeatureMediatorModule.Providers::class
    ]
)
object CategoryListFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindCategoryListFeatureArgs(
            categoryListFeatureMediator: CategoryListFeatureMediator
        ): CategoryListFeatureArgs

        @Binds
        @GlobalScope
        fun bindCategoryListFeatureCallback(
            categoryListFeatureMediator: CategoryListFeatureMediator
        ): CategoryListFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCategoryListFeatureRunner(
            categoryListFeatureMediator: CategoryListFeatureMediator
        ): CategoryListFeatureRunner =
            categoryListFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCategorySavingFeatureMediator(
            categorySavingFeatureRunner: CategorySavingFeatureRunner,
            productListFeatureRunner: ProductListFeatureRunner,
            globalRouter: GlobalRouter
        ): CategoryListFeatureMediator =
            CategoryListFeatureMediator(
                categorySavingFeatureRunner = categorySavingFeatureRunner,
                productListFeatureRunner = productListFeatureRunner,
                router = globalRouter
            )
    }
}