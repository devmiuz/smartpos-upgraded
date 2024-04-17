package uz.uzkassa.smartpos.feature.product.list.data.repository.save

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.feature.product.list.data.repository.save.params.SaveProductParams
import uz.uzkassa.smartpos.feature.product.list.dependencies.ProductListFeatureArgs
import java.math.BigDecimal
import javax.inject.Inject

internal class ProductSavingRepositoryImpl @Inject constructor(
    private val productEntityDao: ProductEntityDao,
    private val productListFeatureArgs: ProductListFeatureArgs,
    private val productRelationDao: ProductRelationDao,
    private val productRestService: ProductRestService
) : ProductSavingRepository {

    @FlowPreview
    override fun updateProduct(params: SaveProductParams): Flow<Product> {
        val branchId: Long = productListFeatureArgs.branchId
        return productRestService.setProductPrice(params.asJsonElement(branchId))
            .onEach {
                productEntityDao.updateSalePriceByProductId(
                    productId = params.productId,
                    salePrice = params.salesPrice ?: BigDecimal.ZERO
                )
            }
            .flatMapConcat { flow { emit(productRelationDao.getRelationByProductId(params.productId)) } }
            .mapNotNull { it?.map() }
    }
}