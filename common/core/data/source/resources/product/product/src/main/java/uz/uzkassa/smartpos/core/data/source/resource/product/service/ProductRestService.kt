package uz.uzkassa.smartpos.core.data.source.resource.product.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductResponse
import uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination.ProductPaginationResponse
import uz.uzkassa.smartpos.core.data.source.resource.product.preference.ProductSyncTimePreference

interface ProductRestService {

    fun addProductToFavorites(productId: Long, branchId: Long): Flow<Unit>

    fun createProduct(branchId: Long, jsonElement: JsonElement): Flow<ProductResponse>

    fun deleteProductFromFavorites(productId: Long, branchId: Long): Flow<Unit>

    fun getProductByBarcode(branchId: Long, barcode: String): Flow<ProductResponse>

    fun getProductByProductId(productId: Long, branchId: Long): Flow<ProductResponse>

    fun updateProduct(branchId: Long, jsonElement: JsonElement): Flow<ProductResponse>

    fun setProductPrice(jsonElement: JsonElement): Flow<Unit>

    fun getProductsByBranchId(branchId: Long): Flow<List<ProductResponse>>

    fun getProductsByBranchId(
        branchId: Long,
        page: Int,
        size: Int,
        lastModifiedDate: String? = null
    ): Flow<ProductPaginationResponse>

    fun getProductsByCategoryId(branchId: Long, categoryId: Long): Flow<List<ProductResponse>>

    fun getProductsByLastModifiedDate(branchId: Long): Flow<List<ProductResponse>>

    companion object {

        fun instantiate(
            retrofit: Retrofit,
            syncTimePreference: ProductSyncTimePreference
        ): ProductRestService =
            ProductRestServiceImpl(retrofit.create(), syncTimePreference)
    }
}