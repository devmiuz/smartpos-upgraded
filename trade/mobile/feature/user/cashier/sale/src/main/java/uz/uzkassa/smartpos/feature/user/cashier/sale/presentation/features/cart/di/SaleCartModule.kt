package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.manager.scanner.BarcodeScannerManager
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.draft.ReceiptDraftProductsBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.held.ReceiptHeldBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.sale.SaleCartBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.quantity.ProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.marking.ProductMarkingRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.marking.ProductMarkingRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.ProductInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.marking.ProductMarkingInteractor

@Module(includes = [SaleCartModule.Binders::class, SaleCartModule.Providers::class])
internal object SaleCartModule {

    @Module
    interface Binders {

        @Binds
        @SaleCartScope
        fun bindProductRepository(
            impl: ProductRepositoryImpl
        ): ProductRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @SaleCartScope
        @FlowPreview
        fun provideReceiptHeldFlow(
            receiptHeldBroadcastChannel: ReceiptHeldBroadcastChannel
        ): Flow<Unit> =
            receiptHeldBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @SaleCartScope
        @FlowPreview
        fun provideProductQuantityResultFlow(
            cashierSaleFeatureArgs: CashierSaleFeatureArgs
        ): Flow<ProductQuantity> =
            cashierSaleFeatureArgs
                .productQuantityBroadcastChannel
                .asFlow()
                .filterNotNull()

        @JvmStatic
        @Provides
        @SaleCartScope
        @FlowPreview
        fun provideProductResultFlow(
            barcodeScannerManager: BarcodeScannerManager,
            productInteractor: ProductInteractor
        ): Flow<Result<Product>> =
            barcodeScannerManager
                .getResult()
                .filter { it.isNotEmpty() }
                .flatMapMerge { productInteractor.getProductByBarcode(it) }

        @JvmStatic
        @Provides
        @SaleCartScope
        @FlowPreview
        fun provideProductsFlow(
            receiptDraftProductsBroadcastChannel: ReceiptDraftProductsBroadcastChannel
        ): Flow<List<SaleCart.ItemType.Product>> =
            receiptDraftProductsBroadcastChannel.asFlow()

        @ExperimentalCoroutinesApi
        @JvmStatic
        @Provides
        @SaleCartScope
        @FlowPreview
        fun provideSaleProductQuantity(
            coroutineContextManager: CoroutineContextManager,
            productInteractor: ProductInteractor,
            cashierSaleFeatureArgs: CashierSaleFeatureArgs
        ): Flow<SaleProductQuantity> =
            cashierSaleFeatureArgs
                .productMarkingResultBroadcastChannel
                .asFlow()
                .filterNotNull()
                .flatMapConcat { element ->
                    return@flatMapConcat when {
                        element.productId != null -> {
                            return@flatMapConcat productInteractor
                                .getProductByProductId(checkNotNull(element.productId))
                                .map { SaleProductQuantity(element, it) }
                                .flowOn(coroutineContextManager.ioContext)
                        }
                        element.uid != null -> flowOf(SaleProductQuantity(element))
                        else -> emptyFlow()
                    }
                }

        @JvmStatic
        @Provides
        @SaleCartScope
        fun provideProductInteractor(
            coroutineContextManager: CoroutineContextManager,
            productRepository: ProductRepository
        ): ProductInteractor =
            ProductInteractor(
                coroutineContextManager,
                productRepository
            )


        @JvmStatic
        @Provides
        @SaleCartScope
        fun provideSaleCartBroadcastChannel(): SaleCartBroadcastChannel =
            SaleCartBroadcastChannel()

        @JvmStatic
        @Provides
        @SaleCartScope
        @FlowPreview
        fun provideSaleCartFlow(
            saleCartBroadcastChannel: SaleCartBroadcastChannel
        ): Flow<SaleCart> =
            saleCartBroadcastChannel.asFlow()
    }
}