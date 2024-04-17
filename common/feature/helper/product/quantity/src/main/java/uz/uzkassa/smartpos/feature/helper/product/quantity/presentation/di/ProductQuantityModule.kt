package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.channel.ProductQuantityBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.channel.ProductUnitBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.model.ProductQuantity
import uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies.ProductQuantityFeatureArgs
import uz.uzkassa.smartpos.feature.helper.product.quantity.domain.ProductQuantityInteractor

@Module(includes = [ProductQuantityModule.Providers::class])
internal object ProductQuantityModule {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @ProductQuantityScope
        fun provideProductQuantityInteractor(
            productQuantityBroadcastChannel: ProductQuantityBroadcastChannel,
            productUnitBroadcastChannel: ProductUnitBroadcastChannel,
            productQuantityFeatureArgs: ProductQuantityFeatureArgs
        ): ProductQuantityInteractor =
            ProductQuantityInteractor(
                productQuantityBroadcastChannel = productQuantityBroadcastChannel,
                productUnitBroadcastChannel = productUnitBroadcastChannel,
                productQuantityFeatureArgs = productQuantityFeatureArgs
            )

        @JvmStatic
        @Provides
        @ProductQuantityScope
        fun provideProductQuantityBroadcastChannel(): ProductQuantityBroadcastChannel =
            ProductQuantityBroadcastChannel()

        @Suppress("EXPERIMENTAL_API_USAGE")
        @JvmStatic
        @Provides
        @ProductQuantityScope
        @FlowPreview
        fun provideProductQuantity(
            productQuantityBroadcastChannel: ProductQuantityBroadcastChannel,
            productQuantityInteractor: ProductQuantityInteractor
        ): Flow<ProductQuantity> =
            productQuantityBroadcastChannel.asFlow()
                .onStart { productQuantityBroadcastChannel.offer() }
                .map { if (it.quantity.isNaN()) it.copy(quantity = 0.0) else it }
                .onEach { productQuantityInteractor.setProductQuantity(it) }

        @JvmStatic
        @Provides
        @ProductQuantityScope
        fun provideProductUnitBroadcastChannel(): ProductUnitBroadcastChannel =
            ProductUnitBroadcastChannel()

        @JvmStatic
        @Provides
        @ProductQuantityScope
        @FlowPreview
        fun provideProductUnitFlow(
            productUnitBroadcastChannel: ProductUnitBroadcastChannel
        ): Flow<Unit> =
            productUnitBroadcastChannel.asFlow()
                .map { it.unit }
    }
}