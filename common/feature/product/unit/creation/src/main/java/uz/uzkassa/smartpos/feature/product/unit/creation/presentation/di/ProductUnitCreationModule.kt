package uz.uzkassa.smartpos.feature.product.unit.creation.presentation.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.product.unit.creation.data.channel.ProductUnitBroadcastChannel
import uz.uzkassa.smartpos.feature.product.unit.creation.data.model.ProductUnitDetails
import uz.uzkassa.smartpos.feature.product.unit.creation.dependencies.ProductUnitCreationFeatureArgs
import uz.uzkassa.smartpos.feature.product.unit.creation.domain.ProductUnitCreationInteractor
import uz.uzkassa.smartpos.feature.product.unit.creation.domain.ProductUnitInteractor

@Module(includes = [ProductUnitCreationModule.Providers::class])
internal object ProductUnitCreationModule {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @ProductUnitCreationScope
        fun provideProductUnitCreationBroadcastChannel(
        ): ProductUnitBroadcastChannel = ProductUnitBroadcastChannel()

        @FlowPreview
        @JvmStatic
        @Provides
        @ProductUnitCreationScope
        fun provideProductUnitsLazyFlow(
            productUnitBroadcastChannel: ProductUnitBroadcastChannel
        ): Flow<List<ProductUnitDetails>> =
            productUnitBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @ProductUnitCreationScope
        fun provideProductUnitInteractor(
            productUnitBroadcastChannel: ProductUnitBroadcastChannel
        ): ProductUnitInteractor = ProductUnitInteractor(
            productUnitBroadcastChannel = productUnitBroadcastChannel
        )

        @JvmStatic
        @Provides
        @ProductUnitCreationScope
        fun provideProductUnitCreationInteractor(
            productUnitBroadcastChannel: ProductUnitBroadcastChannel,
            productUnitCreationFeatureArgs: ProductUnitCreationFeatureArgs,
            productUnitInteractor: ProductUnitInteractor
        ): ProductUnitCreationInteractor = ProductUnitCreationInteractor(
            productUnitBroadcastChannel = productUnitBroadcastChannel,
            productUnitCreationFeatureArgs = productUnitCreationFeatureArgs,
            productUnitInteractor = productUnitInteractor
        )

    }
}