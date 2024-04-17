package uz.uzkassa.smartpos.feature.product_marking.presentation.camera_scanner.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.product_marking.data.repository.ProductMarkingRepository
import uz.uzkassa.smartpos.feature.product_marking.data.repository.ProductMarkingRepositoryImpl
import uz.uzkassa.smartpos.feature.product_marking.dependencies.ProductMarkingFeatureArgs
import uz.uzkassa.smartpos.feature.product_marking.domain.ProductMarkingInteractor

@Module(includes = [MarkingCameraScannerModule.Providers::class, MarkingCameraScannerModule.Binders::class])
internal object MarkingCameraScannerModule {

    @Module
    interface Binders {

        @Binds
        @MarkingCameraScannerScope
        fun bindProductMarkingRepository(
            impl: ProductMarkingRepositoryImpl
        ): ProductMarkingRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @MarkingCameraScannerScope
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