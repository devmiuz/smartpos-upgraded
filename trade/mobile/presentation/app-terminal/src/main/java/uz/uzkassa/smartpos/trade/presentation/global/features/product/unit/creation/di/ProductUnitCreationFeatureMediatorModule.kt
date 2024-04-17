package uz.uzkassa.smartpos.trade.presentation.global.features.product.unit.creation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.product.unit.creation.dependencies.ProductUnitCreationFeatureArgs
import uz.uzkassa.smartpos.feature.product.unit.creation.dependencies.ProductUnitCreationFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.product.unit.creation.ProductUnitCreationFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.product.unit.creation.runner.ProductUnitCreationFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        ProductUnitCreationFeatureMediatorModule.Binders::class,
        ProductUnitCreationFeatureMediatorModule.Providers::class
    ]
)
object ProductUnitCreationFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindProductCreationFeatureArgs(
            productUnitCreationFeatureMediator: ProductUnitCreationFeatureMediator
        ): ProductUnitCreationFeatureArgs


        @Binds
        @GlobalScope
        fun bindProductCreationFeatureCallback(
            productUnitCreationFeatureMediator: ProductUnitCreationFeatureMediator
        ): ProductUnitCreationFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun bindProductCreationFeatureRunner(
            productUnitCreationFeatureMediator: ProductUnitCreationFeatureMediator
        ): ProductUnitCreationFeatureRunner =
            productUnitCreationFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideProductUnitFeatureMediator(
            globalRouter: GlobalRouter
        ): ProductUnitCreationFeatureMediator =
            ProductUnitCreationFeatureMediator(globalRouter)
    }
}