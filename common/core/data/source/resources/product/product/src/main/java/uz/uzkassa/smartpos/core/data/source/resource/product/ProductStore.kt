package uz.uzkassa.smartpos.core.data.source.resource.product

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductResponse
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService

class ProductStore(
    private val productEntityDao: ProductEntityDao,
    private val productRelationDao: ProductRelationDao,
    private val productRestService: ProductRestService
) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun getProduct() : Store<Params.Product, Product> {
        return StoreBuilder.from<Params.Product, ProductResponse, Product>(
            fetcher = Fetcher.ofFlow {
                return@ofFlow when {
                    it.barcode != null ->
                        productRestService.getProductByBarcode(it.branchId, it.barcode)
                    it.productId != null ->
                        productRestService.getProductByProductId(it.productId, it.branchId)
                    else -> throw UnsupportedOperationException()
                }
            },
            sourceOfTruth = SourceOfTruth.of(
                nonFlowReader = {
                    return@of when {
                        it.barcode != null ->
                            productRelationDao
                                .getRelationByBarcode(it.barcode, it.allowedForSale)
                                ?.map()
                        it.productId != null ->
                            productRelationDao
                                .getRelationByProductId(it.productId)
                                ?.map()
                        else -> throw UnsupportedOperationException()
                    }
                },
                writer = { _, it -> productEntityDao.save(it) },
                delete = { throw UnsupportedOperationException() },
                deleteAll = { productEntityDao.deleteAll() }
            )
        ).disableCache().build()
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun getProducts() : Store<Params.Products, List<Product>> {
        return StoreBuilder.from<Params.Products, List<ProductResponse>, List<Product>>(
            fetcher = Fetcher.ofFlow {
                return@ofFlow when {
                    it.categoryId != null ->
                        productRestService.getProductsByCategoryId(it.branchId, it.categoryId)
                    it.fromLastModifiedDate ->
                        productRestService.getProductsByLastModifiedDate(it.branchId)
                    else -> productRestService.getProductsByBranchId(it.branchId)
                }
            },
            sourceOfTruth = SourceOfTruth.of(
                nonFlowReader = { params ->
                    productRelationDao
                        .getRelations(params.allowedForSale)
                        .map { it.map() }
                },
                writer = { _, it -> productEntityDao.save(it) },
                delete = { throw UnsupportedOperationException() },
                deleteAll = { productEntityDao.deleteAll() }
            )
        ).disableCache().build()
    }

    sealed class Params {

        data class Product(
            val branchId: Long,
            val productId: Long? = null,
            val barcode: String? = null,
            val allowedForSale: Boolean = false
        )

        data class Products(
            val branchId: Long,
            val categoryId: Long? = null,
            val fromLastModifiedDate: Boolean = false,
            val allowedForSale: Boolean = false
        )
    }
}