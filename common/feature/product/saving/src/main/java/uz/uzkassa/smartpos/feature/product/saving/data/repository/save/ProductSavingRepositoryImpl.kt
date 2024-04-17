package uz.uzkassa.smartpos.feature.product.saving.data.repository.save

import android.util.Log
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.feature.product.saving.data.repository.save.params.SaveProductParams
import uz.uzkassa.smartpos.feature.product.saving.dependencies.ProductSavingFeatureArgs
import javax.inject.Inject

internal class ProductSavingRepositoryImpl @Inject constructor(
    private val productEntityDao: ProductEntityDao,
    private val productRelationDao: ProductRelationDao,
    private val productRestService: ProductRestService,
    productSavingFeatureArgs: ProductSavingFeatureArgs
) : ProductSavingRepository {
    private val branchId: Long = productSavingFeatureArgs.branchId

    @FlowPreview
    override fun createProduct(params: SaveProductParams): Flow<Product> {
        return productRestService.createProduct(branchId, params.asJsonElement(branchId))
            .onEach {
                Log.d("productEntityDao", it.toString())
                productEntityDao.save(it)
            }
            .mapNotNull {
                Log.d("productRelationDao", it.toString())
                productRelationDao.getRelationByProductId(it.id)?.map()
            }
    }

    @FlowPreview
    override fun updateProduct(params: SaveProductParams): Flow<Product> {
        return productRestService.updateProduct(branchId, params.asJsonElement(branchId))
            .onEach {
                productEntityDao.save(it)
            }
            .mapNotNull { productRelationDao.getRelationByProductId(it.id)?.map() }
    }
}