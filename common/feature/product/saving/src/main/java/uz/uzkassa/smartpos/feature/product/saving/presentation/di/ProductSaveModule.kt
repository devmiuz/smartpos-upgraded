package uz.uzkassa.smartpos.feature.product.saving.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.product.saving.data.repository.details.ProductDetailsRepository
import uz.uzkassa.smartpos.feature.product.saving.data.repository.details.ProductDetailsRepositoryImpl
import uz.uzkassa.smartpos.feature.product.saving.data.repository.save.ProductSavingRepository
import uz.uzkassa.smartpos.feature.product.saving.data.repository.save.ProductSavingRepositoryImpl
import uz.uzkassa.smartpos.feature.product.saving.dependencies.ProductSavingFeatureArgs
import uz.uzkassa.smartpos.feature.product.saving.domain.ProductSaveInteractor
import java.math.BigDecimal

@Module(
    includes = [
        ProductSaveModule.Binders::class,
        ProductSaveModule.Providers::class
    ]
)
internal object ProductSaveModule {

    @Module
    interface Binders {

        @Binds
        @ProductSaveScope
        fun bindCategoryRepository(
            impl: ProductDetailsRepositoryImpl
        ): ProductDetailsRepository

        @Binds
        @ProductSaveScope
        fun bindProductSaveRepository(
            impl: ProductSavingRepositoryImpl
        ): ProductSavingRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @ProductSaveScope
        @FlowPreview
        fun provideCategorySelectionFlow(
            productSavingFeatureArgs: ProductSavingFeatureArgs
        ): Flow<Category> =
            productSavingFeatureArgs.categorySelectionBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @ProductSaveScope
        @FlowPreview
        fun provideProductUnitsLazyFlow(
            productSavingFeatureArgs: ProductSavingFeatureArgs
        ): Flow<List<ProductUnit>> =
            productSavingFeatureArgs.productUnitsBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @ProductSaveScope
        fun provideCategorySelectionInteractor(
            coroutineContextManager: CoroutineContextManager,
            productDetailsRepository: ProductDetailsRepository,
            productSavingRepository: ProductSavingRepository
        ): ProductSaveInteractor =
            ProductSaveInteractor(
                coroutineContextManager = coroutineContextManager,
                productDetailsRepository = productDetailsRepository,
                productSavingRepository = productSavingRepository
            )

        @JvmStatic
        @Provides
        @ProductSaveScope
        @FlowPreview
        fun provideVATRateFlow(
            productSavingFeatureArgs: ProductSavingFeatureArgs
        ): Flow<BigDecimal?> =
            productSavingFeatureArgs.productVATRateBroadcastChannel.asFlow()
    }
}