package uz.uzkassa.smartpos.trade.presentation.global.features.category.saving.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.category.saving.dependencies.CategorySavingFeatureArgs
import uz.uzkassa.smartpos.feature.category.saving.dependencies.CategorySavingFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.category.saving.CategorySavingFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.category.saving.runner.CategorySavingFeatureRunner

@Module(
    includes = [
        CategorySavingFeatureMediatorModule.Binders::class,
        CategorySavingFeatureMediatorModule.Providers::class
    ]
)
object CategorySavingFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindCategorySavingFeatureArgs(
            categorySavingFeatureMediator: CategorySavingFeatureMediator
        ): CategorySavingFeatureArgs

        @Binds
        @GlobalScope
        fun bindCategorySavingFeatureCallback(
            categorySavingFeatureMediator: CategorySavingFeatureMediator
        ): CategorySavingFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCategorySavingFeatureRunner(
            categorySavingFeatureMediator: CategorySavingFeatureMediator
        ): CategorySavingFeatureRunner =
            categorySavingFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCategorySavingFeatureMediator(): CategorySavingFeatureMediator =
            CategorySavingFeatureMediator()
    }
}