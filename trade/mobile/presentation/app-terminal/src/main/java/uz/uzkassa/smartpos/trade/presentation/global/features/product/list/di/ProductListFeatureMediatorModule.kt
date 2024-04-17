package uz.uzkassa.smartpos.trade.presentation.global.features.product.list.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.product.list.dependencies.ProductListFeatureArgs
import uz.uzkassa.smartpos.feature.product.list.dependencies.ProductListFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.product.list.ProductListFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.product.list.runner.ProductListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.product.saving.runner.ProductSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        ProductListFeatureMediatorModule.Binders::class,
        ProductListFeatureMediatorModule.Providers::class
    ]
)
object ProductListFeatureMediatorModule {

    @Module
    interface Binders {
        @Binds
        @GlobalScope
        fun bindProductListFeatureArgs(
            productListFeatureMediator: ProductListFeatureMediator
        ): ProductListFeatureArgs

        @Binds
        @GlobalScope
        fun bindProductListFeatureCallback(
            productListFeatureMediator: ProductListFeatureMediator
        ): ProductListFeatureCallback

    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideProductListFeatureRunner(
            productListFeatureMediator: ProductListFeatureMediator
        ): ProductListFeatureRunner =
            productListFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideProductListFeatureMediator(
            productSavingFeatureRunner: ProductSavingFeatureRunner,
            globalRouter: GlobalRouter
        ): ProductListFeatureMediator =
            ProductListFeatureMediator(
                productSavingFeatureRunner = productSavingFeatureRunner,
                router = globalRouter
            )
    }
}