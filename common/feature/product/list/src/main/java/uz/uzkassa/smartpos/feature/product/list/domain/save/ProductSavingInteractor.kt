package uz.uzkassa.smartpos.feature.product.list.domain.save

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.product.list.data.channel.HasEnabledProductsBroadcastChannel
import uz.uzkassa.smartpos.feature.product.list.data.repository.save.ProductSavingRepository
import uz.uzkassa.smartpos.feature.product.list.data.repository.save.params.SaveProductParams
import java.math.BigDecimal
import javax.inject.Inject

internal class ProductSavingInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val hasEnabledProductsBroadcastChannel: HasEnabledProductsBroadcastChannel,
    private val productSavingRepository: ProductSavingRepository
) {
    private val productsMap: MutableMap<Long, Product> = hashMapOf()
    private val updatedProducts: MutableSet<Product> = hashSetOf()

    fun setProducts(products: List<Product>) {
        productsMap.putAll(products.map { it.id to it }.toMap())
        hasEnabledProductsBroadcastChannel.sendBlocking(false)
    }

    fun addProduct(product: Product) {
        productsMap[product.id] = product
        hasEnabledProductsBroadcastChannel.sendBlocking(updatedProducts.isNotEmpty())
    }

    fun updateProduct(product: Product, price: BigDecimal) {
        val cachedProduct: Product? = productsMap[product.id]
        if (cachedProduct != null)
            updatedProducts.find { it.id == product.id }?.let { updatedProducts.remove(it) }

        if (product.salesPrice != price) updatedProducts.add(product.copy(salesPrice = price))
        hasEnabledProductsBroadcastChannel.sendBlocking(updatedProducts.isNotEmpty())
    }

    @FlowPreview
    fun updateProducts(): Flow<Result<Unit>> {
        return updatedProducts.toTypedArray().asFlow()
            .flatMapConcat { product ->
                productSavingRepository
                    .updateProduct(SaveProductParams(product.id, product.salesPrice))
                    .onEach { updatedProducts.add(product) }
            }
            .onEach { hasEnabledProductsBroadcastChannel.send(false) }
            .map { Unit }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}