package uz.uzkassa.smartpos.trade.presentation.global.features.category.selection.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.common.feature.category.selection.dependencies.CategorySelectionFeatureArgs
import uz.uzkassa.common.feature.category.selection.dependencies.CategorySelectionFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.category.selection.CategorySelectionFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.category.selection.runner.CategorySelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        CategorySelectionFeatureMediatorModule.Binders::class,
        CategorySelectionFeatureMediatorModule.Providers::class
    ]
)
object CategorySelectionFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindCategorySelectionFeatureArgs(
            categorySelectionFeatureMediator: CategorySelectionFeatureMediator
        ): CategorySelectionFeatureArgs

        @Binds
        @GlobalScope
        fun bindCategorySelectionFeatureCallback(
            categorySelectionFeatureMediator: CategorySelectionFeatureMediator
        ): CategorySelectionFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCategorySelectionFeatureRunner(
            categorySelectionFeatureMediator: CategorySelectionFeatureMediator
        ): CategorySelectionFeatureRunner =
            categorySelectionFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCategorySelectionFeatureMediator(
            globalRouter: GlobalRouter
        ): CategorySelectionFeatureMediator =
            CategorySelectionFeatureMediator(globalRouter)
    }
}