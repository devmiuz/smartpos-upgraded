package uz.uzkassa.smartpos.trade.presentation.global.features.product.saving.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.product.saving.dependencies.ProductSavingFeatureArgs
import uz.uzkassa.smartpos.feature.product.saving.dependencies.ProductSavingFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.category.selection.runner.CategorySelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.company.vat.runner.CompanyVATSelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.product.saving.ProductSavingFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.product.saving.runner.ProductSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.product.unit.creation.runner.ProductUnitCreationFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        ProductSavingFeatureMediatorModule.Binders::class,
        ProductSavingFeatureMediatorModule.Providers::class
    ]
)
object ProductSavingFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindProductSavingFeatureArgs(
            productSavingFeatureMediator: ProductSavingFeatureMediator
        ): ProductSavingFeatureArgs

        @Binds
        @GlobalScope
        fun bindProductSavingFeatureCallback(
            productSavingFeatureMediator: ProductSavingFeatureMediator
        ): ProductSavingFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideProductSavingFeatureRunner(
            productSavingFeatureMediator: ProductSavingFeatureMediator
        ): ProductSavingFeatureRunner =
            productSavingFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideProductSavingFeatureMediator(
            categorySelectionFeatureRunner: CategorySelectionFeatureRunner,
            companyVATSelectionFeatureRunner: CompanyVATSelectionFeatureRunner,
            productUnitCreationFeatureRunner: ProductUnitCreationFeatureRunner,
            globalRouter: GlobalRouter
        ): ProductSavingFeatureMediator =
            ProductSavingFeatureMediator(
                categorySelectionFeatureRunner,
                companyVATSelectionFeatureRunner,
                productUnitCreationFeatureRunner,
                globalRouter
            )
    }
}