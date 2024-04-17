package uz.uzkassa.smartpos.feature.product_marking.data.repository

import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.marking.dao.ProductMarkingDao
import uz.uzkassa.smartpos.core.data.source.resource.marking.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.marking.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarking
import javax.inject.Inject

internal class ProductMarkingRepositoryImpl @Inject constructor(
    private val productMarkingDao: ProductMarkingDao
) : ProductMarkingRepository {

    override fun getProductMarking(productId: Long?): Flow<ProductMarking?> {
        if (productId == null) return flowOf(null)
        return flow { emit(productMarkingDao.getProductMarkingByProductId(productId).first()) }
            .map { it?.map() }
    }

    override fun getAllProductMarkings(): Flow<List<ProductMarking>> {
        return flow {
            emit(
                productMarkingDao.getAllProductMarkings()
                    .first()
            )
        }
            .map { it.map() }
    }

    override fun updateOrInsertProductMarking(productMarkings: List<ProductMarking>) {
        productMarkingDao.upsert(productMarkings.mapToEntity())
    }
}