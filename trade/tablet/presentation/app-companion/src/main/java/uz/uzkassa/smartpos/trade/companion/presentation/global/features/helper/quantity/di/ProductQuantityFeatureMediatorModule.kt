package uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.quantity.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies.ProductQuantityFeatureArgs
import uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies.ProductQuantityFeatureCallback
import uz.uzkassa.smartpos.trade.companion.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.quantity.ProductQuantityFeatureMediator
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.quantity.runner.ProductQuantityFeatureRunner

@Module(
    includes = [
        ProductQuantityFeatureMediatorModule.Binders::class,
        ProductQuantityFeatureMediatorModule.Providers::class
    ]
)
object ProductQuantityFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindQuantityFeatureArgs(
            productQuantityFeatureMediator: ProductQuantityFeatureMediator
        ): ProductQuantityFeatureArgs

        @Binds
        @GlobalScope
        fun bindQuantityFeatureCallback(
            productQuantityFeatureMediator: ProductQuantityFeatureMediator
        ): ProductQuantityFeatureCallback

    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideProductQuantityFeatureRunner(
            productQuantityFeatureMediator: ProductQuantityFeatureMediator
        ): ProductQuantityFeatureRunner =
            productQuantityFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideProductQuantityFeatureMediator(
        ): ProductQuantityFeatureMediator =
            ProductQuantityFeatureMediator()

    }
}