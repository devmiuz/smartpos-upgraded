package uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitEntity
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitResponse

@Dao
abstract class ProductUnitEntityDao : BaseDao.Impl<ProductUnitEntity>() {

    @Query(value = "DELETE FROM product_units WHERE product_unit_product_id IN (:productIds)")
    internal abstract fun deleteByProductIds(productIds: LongArray)

    @Query(value = "DELETE FROM product_units WHERE product_unit_product_id = :productId")
    internal abstract fun deleteByProductId(productId: Long)

    fun save(productId: Long, responses: List<ProductUnitResponse>?) {
        val entities: List<ProductUnitEntity> = responses?.mapToEntities() ?: return
        deleteByProductId(productId)
        insertOrUpdate(entities)
    }

    fun save(productIds: LongArray, responses: List<ProductUnitResponse>?) {
        val entities: List<ProductUnitEntity> = responses?.mapToEntities() ?: return
        deleteProductUnitsByProductIds(productIds)
        insertOrUpdate(entities)
    }

    private fun deleteProductUnitsByProductIds(productIds: LongArray) {
        val productIdsSublist: List<LongArray> =
            productIds.toList().chunked(500).map { it.toLongArray() }
        productIdsSublist.forEach { deleteByProductIds(it) }
    }
}