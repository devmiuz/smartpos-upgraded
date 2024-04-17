package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductPaginationRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs
import javax.inject.Inject

internal class ProductRepositoryImpl @Inject constructor(
    cashierSaleFeatureArgs: CashierSaleFeatureArgs,
    private val productEntityDao: ProductEntityDao,
    private val productPaginationRelationDao: ProductPaginationRelationDao,
    private val productRelationDao: ProductRelationDao,
    private val productRestService: ProductRestService
) : ProductRepository {
    private val branchId: Long = cashierSaleFeatureArgs.branchId

    @FlowPreview
    override fun addToFavoritesByProductId(productId: Long): Flow<Unit> {
        return productRestService.addProductToFavorites(productId, branchId)
            .onEach { productEntityDao.updateFavoriteByProductId(productId, true) }
    }

    @FlowPreview
    override fun deleteFromFavoritesByProductId(productId: Long): Flow<Unit> {
        return productRestService.deleteProductFromFavorites(productId, branchId)
            .onEach { productEntityDao.updateFavoriteByProductId(productId, false) }
    }

    @FlowPreview
    override fun getFavoriteProducts(page: Int): Flow<List<Product>> {
        return flow { emit(productPaginationRelationDao.getRelationsByFavorites(page, SIZE)) }
            .map { it -> it.productRelations.map().sortedBy { it.name } }
    }

    @FlowPreview
    override fun getProductByBarcode(barcode: String): Flow<Product?> {
        return flow { emit(productRelationDao.getRelationByBarcode(barcode, true)) }
            .map { it?.map() }
    }

    @FlowPreview
    override fun getProductByProductId(productId: Long): Flow<Product> {
        return flow { emit(productRelationDao.getRelationByProductId(productId)) }
            .switch {
                productRestService.getProductByProductId(productId, branchId)
                    .onEach { productEntityDao.save(it) }
                    .map { productRelationDao.getRelationByProductId(productId) }
            }
            .mapNotNull { it?.map() }
    }

    @FlowPreview
    override fun findProductsByName(name: String, page: Int): Flow<List<Product>> {
        return flow { emit(productPaginationRelationDao.getRelationsByName(name, page, SIZE)) }
            .map { it -> it.productRelations.map().sortedBy { it.name } }
    }

    private companion object {
        const val SIZE: Int = 20
    }
}