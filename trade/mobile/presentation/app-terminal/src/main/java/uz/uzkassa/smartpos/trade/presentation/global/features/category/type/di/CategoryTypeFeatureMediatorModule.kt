package uz.uzkassa.smartpos.trade.presentation.global.features.category.type.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.category.type.dependencies.CategoryTypeFeatureArgs
import uz.uzkassa.smartpos.feature.category.type.dependencies.CategoryTypeFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.category.list.runner.CategoryListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.category.main.runner.MainCategoriesFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.category.type.CategoryTypeFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.category.type.runner.CategoryTypeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        CategoryTypeFeatureMediatorModule.Binders::class,
        CategoryTypeFeatureMediatorModule.Providers::class
    ]
)
object CategoryTypeFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindCategoryTypeFeatureArgs(
            categoryTypeFeatureMediator: CategoryTypeFeatureMediator
        ): CategoryTypeFeatureArgs

        @Binds
        @GlobalScope
        fun bindCategoryTypeFeatureCallback(
            categoryTypeFeatureMediator: CategoryTypeFeatureMediator
        ): CategoryTypeFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCategoryTypeFeatureRunner(
            categoryTypeFeatureMediator: CategoryTypeFeatureMediator
        ): CategoryTypeFeatureRunner =
            categoryTypeFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCategoryTypeFeatureMediator(
            categoryListFeatureRunner: CategoryListFeatureRunner,
            globalRouter: GlobalRouter,
            mainCategoriesFeatureRunner: MainCategoriesFeatureRunner
        ): CategoryTypeFeatureMediator =
            CategoryTypeFeatureMediator(
                categoryListFeatureRunner = categoryListFeatureRunner,
                mainCategoriesFeatureRunner = mainCategoriesFeatureRunner,
                router = globalRouter
            )
    }
}