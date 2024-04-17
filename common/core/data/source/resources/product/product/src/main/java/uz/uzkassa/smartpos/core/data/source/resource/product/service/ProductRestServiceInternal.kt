package uz.uzkassa.smartpos.core.data.source.resource.product.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.http.*
import uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination.ProductPaginationRelation
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductResponse
import uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination.ProductPaginationResponse

internal interface ProductRestServiceInternal {

    @POST("$API_PRODUCTS/{$PATH_ID}/$PATH_FAVORITES")
    fun addProductToFavorites(
        @Path(PATH_ID) productId: Long,
        @Query(QUERY_BRANCH_ID) branchId: Long
    ): Flow<Unit>

    @POST(API_PRODUCTS)
    fun createProduct(
        @Body jsonElement: JsonElement
    ): Flow<ProductResponse>

    @DELETE("$API_PRODUCTS/{$PATH_ID}/$PATH_FAVORITES")
    fun deleteProductFromFavorites(
        @Path(PATH_ID) productId: Long,
        @Query(QUERY_BRANCH_ID) branchId: Long
    ): Flow<Unit>

    @GET("$API_PRODUCTS/{$PATH_BARCODE}/{$PATH_ID}")
    fun getProductByBarcode(
        @Query(QUERY_BRANCH_ID) branchId: Long,
        @Path(PATH_ID) barcode: String
    ): Flow<ProductResponse>

    @GET("$API_PRODUCTS/{$PATH_ID}")
    fun getProductByProductId(
        @Path(PATH_ID) productId: Long,
        @Query(QUERY_BRANCH_ID) branchId: Long
    ): Flow<ProductResponse>

    @PUT(API_PRODUCTS)
    fun updateProduct(
        @Body jsonElement: JsonElement
    ): Flow<ProductResponse>

    @PUT(API_PRODUCTS_SET_PRICE)
    fun setProductPrice(
        @Body jsonElement: JsonElement
    ): Flow<Unit>

    @GET(API_PRODUCTS)
    fun getProductsByBranchId(
        @Query(QUERY_BRANCH_ID) branchId: Long
    ): Flow<List<ProductResponse>>

    @GET(API_PRODUCTS_PAGEABLE)
    fun getProductsByBranchId(
        @Query(QUERY_BRANCH_ID) branchId: Long,
        @Query(QUERY_FROM) lastModifiedDate: String?,
        @Query(QUERY_PAGE) page: Int,
        @Query(QUERY_SIZE) size: Int
    ): Flow<ProductPaginationResponse>

    @GET(API_PRODUCTS)
    fun getProductsByCategoryId(
        @Query(QUERY_BRANCH_ID) branchId: Long,
        @Query(QUERY_CATEGORY_ID) categoryId: Long
    ): Flow<List<ProductResponse>>

    private companion object {
        const val API_PRODUCTS: String = "api/products"
        const val API_PRODUCTS_PAGEABLE: String = "api/products/page"
        const val API_PRODUCTS_SET_PRICE: String = "api/products/set-price"
        const val PATH_FAVORITES: String = "favorites"
        const val PATH_BARCODE: String = "barcode"
        const val PATH_ID: String = "id"
        const val QUERY_BRANCH_ID: String = "branchId"
        const val QUERY_CATEGORY_ID: String = "categoryId"
        const val QUERY_FROM: String = "from"
        const val QUERY_PAGE: String = "page"
        const val QUERY_SIZE: String = "size"
    }
}