package uz.uzkassa.smartpos.feature.product_marking.presentation.scanner.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.product_marking.data.repository.ProductMarkingRepository
import uz.uzkassa.smartpos.feature.product_marking.data.repository.ProductMarkingRepositoryImpl
import uz.uzkassa.smartpos.feature.product_marking.dependencies.ProductMarkingFeatureArgs
import uz.uzkassa.smartpos.feature.product_marking.domain.ProductMarkingInteractor

@Module(includes = [MarkingScannerModule.Binders::class, MarkingScannerModule.Providers::class])
internal object MarkingScannerModule {
    @Module
    interface Binders {

        @Binds
        @MarkingScannerScope
        fun bindProductMarkingRepository(
                impl: ProductMarkingRepositoryImpl
        ): ProductMarkingRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @MarkingScannerScope
        fun provideProductMarkingInteractor(
                coroutineContextManager: CoroutineContextManager,
                productMarkingRepository: ProductMarkingRepository,
                productMarkingFeatureArgs: ProductMarkingFeatureArgs
        ): ProductMarkingInteractor =
                ProductMarkingInteractor(
                        coroutineContextManager,
                        productMarkingRepository,
                        productMarkingFeatureArgs
                )
    }
}