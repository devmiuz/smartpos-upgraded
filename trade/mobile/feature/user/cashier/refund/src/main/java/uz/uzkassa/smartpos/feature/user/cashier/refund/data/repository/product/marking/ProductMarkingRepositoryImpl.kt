package uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.product.marking

import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.marking.dao.ProductMarkingDao
import uz.uzkassa.smartpos.core.data.source.resource.marking.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.marking.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarking
import javax.inject.Inject

internal class ProductMarkingRepositoryImpl @Inject constructor(
    private val productMarkingDao: ProductMarkingDao
) : ProductMarkingRepository {

    override fun getProductMarkings(productId: Long?): Flow<List<ProductMarking>> {
        if (productId == null) return flowOf(emptyList())
        return flow {
            emit(productMarkingDao.getProductMarkingsByProductId(productId).first())
        }
            .map {
                it.map()
            }
    }

    override fun updateOrInsertProductMarking(productMarking: ProductMarking) {
        productMarkingDao.upsert(productMarking.map())
    }

    override fun updateOrInsertProductMarkings(productMarkings: List<ProductMarking>) {
        productMarkingDao.upsert(productMarkings.mapToEntity())
    }

    override fun deleteMarking(productMarking: ProductMarking) {
        productMarking.productId?.let {
            productMarkingDao.deleteProductMarkingByProductId(
                productId = it,
                marking = productMarking.marking
            )
        }
    }
}