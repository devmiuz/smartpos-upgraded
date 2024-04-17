package uz.uzkassa.smartpos.trade.presentation.global.features.marking.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.product_marking.dependencies.ProductMarkingFeatureArgs
import uz.uzkassa.smartpos.feature.product_marking.dependencies.ProductMarkingFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.marking.ProductMarkingFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.marking.runner.ProductMarkingFeatureRunner

@Module(
    includes = [
        ProductMarkingFeatureMediatorModule.Binders::class,
        ProductMarkingFeatureMediatorModule.Providers::class
    ]
)
object ProductMarkingFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindProductMarkingFeatureArgs(
            productMarkingFeatureFeatureMediator: ProductMarkingFeatureMediator
        ): ProductMarkingFeatureArgs

        @Binds
        @GlobalScope
        fun bindProductMarkingFeatureCallback(
            productMarkingFeatureFeatureMediator: ProductMarkingFeatureMediator
        ): ProductMarkingFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideProductQuantityFeatureRunner(
            ProductMarkingFeatureMediator: ProductMarkingFeatureMediator
        ): ProductMarkingFeatureRunner = ProductMarkingFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideProductMarkingFeatureMediator(): ProductMarkingFeatureMediator =
            ProductMarkingFeatureMediator()
    }
}