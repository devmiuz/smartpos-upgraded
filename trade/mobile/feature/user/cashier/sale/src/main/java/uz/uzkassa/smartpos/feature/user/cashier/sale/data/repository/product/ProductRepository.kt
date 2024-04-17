package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product

internal interface ProductRepository {

    fun addToFavoritesByProductId(productId: Long): Flow<Unit>

    fun deleteFromFavoritesByProductId(productId: Long): Flow<Unit>

    fun getFavoriteProducts(page: Int): Flow<List<Product>>

    fun getProductByBarcode(barcode: String): Flow<Product?>

    fun getProductByProductId(productId: Long): Flow<Product?>

    fun findProductsByName(name: String, page: Int): Flow<List<Product>>
}