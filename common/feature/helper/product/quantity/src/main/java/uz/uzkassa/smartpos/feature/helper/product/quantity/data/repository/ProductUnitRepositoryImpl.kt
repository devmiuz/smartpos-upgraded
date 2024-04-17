package uz.uzkassa.smartpos.feature.helper.product.quantity.data.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies.ProductQuantityFeatureArgs
import javax.inject.Inject

internal class ProductUnitRepositoryImpl @Inject constructor(
    productQuantityFeatureArgs: ProductQuantityFeatureArgs,
    private val productUnitRelationDao: ProductUnitRelationDao
) : ProductUnitRepository {
    private val productId: Long? = productQuantityFeatureArgs.productId

    @FlowPreview
    override fun getProductUnits(): Flow<List<ProductUnit>?> {
        if (productId == null) return flowOf(null)
        return flow { emit(productUnitRelationDao.getRelationsByProductId(productId)) }
            .map { if (it.isNullOrEmpty()) null else it.map() }
    }

    override fun getProductUnit(unitId: Long): Flow<ProductUnit?> {
        if (productId == null) return flowOf(null)
        return flow { emit(productUnitRelationDao.getRelationByUnitId(productId, unitId)) }
            .map { it?.map() }
    }
}