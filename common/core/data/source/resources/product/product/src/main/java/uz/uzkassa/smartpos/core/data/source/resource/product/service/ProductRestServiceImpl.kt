package uz.uzkassa.smartpos.core.data.source.resource.product.service

import android.util.Log
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductResponse
import uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination.ProductPaginationResponse
import uz.uzkassa.smartpos.core.data.source.resource.product.preference.ProductSyncTimePreference
import java.util.*
import java.util.concurrent.TimeUnit

internal class ProductRestServiceImpl(
    private val restServiceInternal: ProductRestServiceInternal,
    private val syncTimePreference: ProductSyncTimePreference
) : ProductRestService {

    override fun addProductToFavorites(productId: Long, branchId: Long): Flow<Unit> {
        return restServiceInternal.addProductToFavorites(productId, branchId)
    }

    @FlowPreview
    override fun createProduct(branchId: Long, jsonElement: JsonElement): Flow<ProductResponse> {
        return restServiceInternal.createProduct(jsonElement)
            .onEach { delay(TimeUnit.SECONDS.toMillis(10)) }
            .flatMapConcat { getProductByProductId(it.id, branchId) }
    }

    override fun deleteProductFromFavorites(productId: Long, branchId: Long): Flow<Unit> {
        return restServiceInternal.deleteProductFromFavorites(productId, branchId)
    }

    override fun getProductByBarcode(branchId: Long, barcode: String): Flow<ProductResponse> {
        return restServiceInternal.getProductByBarcode(branchId, barcode)
    }

    override fun getProductByProductId(productId: Long, branchId: Long): Flow<ProductResponse> {
        return restServiceInternal.getProductByProductId(productId, branchId)
    }

    @FlowPreview
    override fun updateProduct(branchId: Long, jsonElement: JsonElement): Flow<ProductResponse> {
        return restServiceInternal.updateProduct(jsonElement)
            .onEach { delay(TimeUnit.SECONDS.toMillis(10)) }
            .flatMapConcat { getProductByProductId(it.id, branchId) }
    }

    override fun setProductPrice(jsonElement: JsonElement): Flow<Unit> {
        return restServiceInternal.setProductPrice(jsonElement)
    }

    override fun getProductsByBranchId(branchId: Long): Flow<List<ProductResponse>> {
        return restServiceInternal.getProductsByBranchId(branchId)
    }

    override fun getProductsByBranchId(
        branchId: Long,
        page: Int,
        size: Int,
        lastModifiedDate: String?
    ): Flow<ProductPaginationResponse> {
        return restServiceInternal.getProductsByBranchId(branchId, lastModifiedDate, page, size)
    }

    @FlowPreview
    private fun getProductsByBranchIdWithPagination(
        branchId: Long,
        date: String?,
        page: Int = PAGEABLE_DEFAULT_PAGE,
        size: Int = PAGEABLE_DEFAULT_SIZE,
        responses: List<ProductResponse> = listOf()
    ): Flow<ProductPaginationResponse> {
        return restServiceInternal.getProductsByBranchId(branchId, date, page, size)
            .flatMapConcat {
                val list = responses.toMutableList().apply { addAll(it.products) }
                return@flatMapConcat if (!it.isLast)
                    getProductsByBranchIdWithPagination(branchId, date, page + 1, size, list)
                else flowOf(it.copy(products = list))
            }
    }

    override fun getProductsByCategoryId(
        branchId: Long,
        categoryId: Long
    ): Flow<List<ProductResponse>> {
        return restServiceInternal.getProductsByCategoryId(branchId, categoryId)
    }

    @FlowPreview
    override fun getProductsByLastModifiedDate(branchId: Long): Flow<List<ProductResponse>> {
        return flowOf(syncTimePreference.lastSyncTime)
            .flatMapConcat { it ->
                getProductsByBranchIdWithPagination(branchId, it)
                    .map { it.products }
                    .onEach { syncTimePreference.setLastSyncTime(Date()) }
                    .catch {
                        if (it !is SerializationException) getProductsByBranchId(branchId)
                        else throw it
                    }
            }
    }

    private companion object {
        const val PAGEABLE_DEFAULT_PAGE: Int = 0
        const val PAGEABLE_DEFAULT_SIZE: Int = 500
    }
}