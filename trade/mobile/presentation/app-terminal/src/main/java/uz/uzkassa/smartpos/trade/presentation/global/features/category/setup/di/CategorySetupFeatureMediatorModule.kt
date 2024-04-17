package uz.uzkassa.smartpos.trade.presentation.global.features.category.setup.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.category.selection.setup.dependencies.CategorySetupFeatureArgs
import uz.uzkassa.smartpos.feature.category.selection.setup.dependencies.CategorySetupFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.category.setup.CategorySetupFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.category.setup.runner.CategorySetupFeatureRunner

@Module(
    includes = [
        CategorySetupFeatureMediatorModule.Binders::class,
        CategorySetupFeatureMediatorModule.Providers::class
    ]
)
object CategorySetupFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindCategorySetupFeatureArgs(
            categorySetupFeatureMediator: CategorySetupFeatureMediator
        ): CategorySetupFeatureArgs

        @Binds
        @GlobalScope
        fun bindCategorySetupFeatureCallback(
            categorySetupFeatureMediator: CategorySetupFeatureMediator
        ): CategorySetupFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCategorySetupFeatureRunner(
            categorySetupFeatureMediator: CategorySetupFeatureMediator
        ): CategorySetupFeatureRunner =
            categorySetupFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCategorySetupFeatureMediator(): CategorySetupFeatureMediator =
            CategorySetupFeatureMediator()
    }
}