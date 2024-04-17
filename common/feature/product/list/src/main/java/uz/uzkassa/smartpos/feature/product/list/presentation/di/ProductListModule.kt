package uz.uzkassa.smartpos.feature.product.list.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.feature.product.list.data.channel.HasEnabledProductsBroadcastChannel
import uz.uzkassa.smartpos.feature.product.list.data.repository.list.ProductListRepository
import uz.uzkassa.smartpos.feature.product.list.data.repository.list.ProductListRepositoryImpl
import uz.uzkassa.smartpos.feature.product.list.data.repository.save.ProductSavingRepository
import uz.uzkassa.smartpos.feature.product.list.data.repository.save.ProductSavingRepositoryImpl
import uz.uzkassa.smartpos.feature.product.list.dependencies.ProductListFeatureArgs

@Module(includes = [ProductListModule.Binders::class, ProductListModule.Providers::class])
internal object ProductListModule {

    @Module
    interface Binders {

        @Binds
        @ProductListScope
        fun bindProductRepository(
            impl: ProductListRepositoryImpl
        ): ProductListRepository

        @Binds
        @ProductListScope
        fun bindProductSavingRepository(
            impl: ProductSavingRepositoryImpl
        ): ProductSavingRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @ProductListScope
        fun provideHasEnabledProductsBroadcastChannel(): HasEnabledProductsBroadcastChannel =
            HasEnabledProductsBroadcastChannel()

        @FlowPreview
        @JvmStatic
        @Provides
        @ProductListScope
        fun provideHasEnabledProductsFlow(
            hasEnabledProductsBroadcastChannel: HasEnabledProductsBroadcastChannel
        ): Flow<Boolean> =
            hasEnabledProductsBroadcastChannel.asFlow()

        @FlowPreview
        @JvmStatic
        @Provides
        @ProductListScope
        fun provideProductFlow(
            productListFeatureArgs: ProductListFeatureArgs
        ): Flow<Product> =
            productListFeatureArgs.productBroadcastChannel.asFlow()
               /*.filter { it.category?.id == productListFeatureArgs.categoryId }*/
    }
}