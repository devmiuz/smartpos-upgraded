package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.exception.product.ProductNotFoundException
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepository
import javax.inject.Inject

internal class ProductByBarcodeInteractor @Inject constructor(
    private val productRepository: ProductRepository,
    private val coroutineContextManager: CoroutineContextManager
) {

    @FlowPreview
    fun getProductByBarcode(barcode: String): Flow<Result<Product>> {
        return productRepository
            .getProductByBarcode(barcode)
            .flatMapResult()
            .flatMapConcat { it ->
                it.getOrNull()?.let { flowOf(Result.success(it)) }
                    ?: flowOf(Result.failure(ProductNotFoundException()))
            }
            .flowOn(coroutineContextManager.ioContext)
    }

    fun getProductByProductId(productId: Long): Flow<Product> {
        return productRepository
            .getProductByProductId(productId)
            .map { it ?: throw ProductNotFoundException() }
            .flowOn(coroutineContextManager.ioContext)
    }
}