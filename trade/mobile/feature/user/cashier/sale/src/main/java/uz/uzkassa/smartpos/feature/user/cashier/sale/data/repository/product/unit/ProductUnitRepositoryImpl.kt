package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.unit

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import javax.inject.Inject

internal class ProductUnitRepositoryImpl @Inject constructor(
    private val productUnitRelationDao: ProductUnitRelationDao
) : ProductUnitRepository {

    @FlowPreview
    override fun getProductUnitsByProductId(productId: Long): Flow<List<ProductUnit>?> {
        return flow { emit(productUnitRelationDao.getRelationsByProductId(productId)) }
            .map { if (it.isNullOrEmpty()) null else it.map() }
    }

    override fun getProductUnitByUnitId(productId: Long, unitId: Long): Flow<ProductUnit?> {
        return flow { emit(productUnitRelationDao.getRelationByUnitId(productId, unitId)) }
            .map { it?.map() }
    }
}