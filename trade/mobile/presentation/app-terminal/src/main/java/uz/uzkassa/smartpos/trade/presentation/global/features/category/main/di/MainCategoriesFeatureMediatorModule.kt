package uz.uzkassa.smartpos.trade.presentation.global.features.category.main.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.category.main.dependencies.MainCategoriesFeatureArgs
import uz.uzkassa.smartpos.feature.category.main.dependencies.MainCategoriesFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.category.main.MainCategoriesFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.category.main.runner.MainCategoriesFeatureRunner

@Module(
    includes = [
        MainCategoriesFeatureMediatorModule.Binders::class,
        MainCategoriesFeatureMediatorModule.Providers::class
    ]
)
object MainCategoriesFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindMainCategoriesFeatureArgs(
            mainCategoriesFeatureMediator: MainCategoriesFeatureMediator
        ): MainCategoriesFeatureArgs

        @Binds
        @GlobalScope
        fun bindMainCategoriesFeatureCallback(
            mainCategoriesFeatureMediator: MainCategoriesFeatureMediator
        ): MainCategoriesFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideMainCategoriesFeatureRunner(
            mainCategoriesFeatureMediator: MainCategoriesFeatureMediator
        ): MainCategoriesFeatureRunner =
            mainCategoriesFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideMainCategoriesFeatureMediator(): MainCategoriesFeatureMediator =
            MainCategoriesFeatureMediator()
    }
}