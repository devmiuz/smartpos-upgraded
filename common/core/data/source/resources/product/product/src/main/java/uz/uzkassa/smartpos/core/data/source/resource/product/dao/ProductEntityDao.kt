package uz.uzkassa.smartpos.core.data.source.resource.product.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.database.SupportRoomDatabase
import uz.uzkassa.smartpos.core.data.source.resource.product.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.product.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductEntity
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductResponse
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.mapper.mapToEntity
import java.math.BigDecimal

@Dao
abstract class ProductEntityDao(database: SupportRoomDatabase) : BaseDao.Impl<ProductEntity>() {
    private val categoryEntityDao = database.getDao<CategoryEntityDao>()
    private val productUnitEntityDao = database.getDao<ProductUnitEntityDao>()
    private val unitEntityDao = database.getDao<UnitEntityDao>()

    @Query(value = "UPDATE products SET product_is_favorite = :isFavorite WHERE product_id = :productId")
    abstract fun updateFavoriteByProductId(productId: Long, isFavorite: Boolean)

    @Query(value = "UPDATE products SET product_sales_price = :salePrice WHERE product_id = :productId")
    abstract fun updateSalePriceByProductId(productId: Long, salePrice: BigDecimal)

    @Query(value = "DELETE FROM products")
    abstract fun deleteAll()

    @Query("DELETE FROM products WHERE product_id NOT IN(:productIdList)")
    abstract fun deleteRemovedByCabinetProducts(productIdList: List<Long>)

    fun save(response: ProductResponse) {
        insertOrUpdate(response.mapToEntity())
        with(response) {
            categoryEntityDao.save(category)
            productUnitEntityDao.save(id, productUnits)
            return@with unit?.let { unitEntityDao.insertOrUpdate(it.mapToEntity()) }
        }
    }

    fun save(responses: List<ProductResponse>) {
//        deleteRemovedByCabinetProducts(responses.map { it.id })
        insertOrUpdate(responses.mapToEntities())
        categoryEntityDao.save(responses.map { it.category })
        productUnitEntityDao.save(
            productIds = responses.map { it.id }.toLongArray(),
            responses = responses.flatMap { it.productUnits ?: emptyList() }
        )
        unitEntityDao.insertOrUpdate(responses.mapNotNull { it.unit?.mapToEntity() })
    }
}