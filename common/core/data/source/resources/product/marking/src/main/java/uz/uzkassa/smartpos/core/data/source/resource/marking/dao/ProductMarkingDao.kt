package uz.uzkassa.smartpos.core.data.source.resource.marking.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarkingEntity

@Dao
abstract class ProductMarkingDao : BaseDao.Impl<ProductMarkingEntity>() {

    @Query(value = "SELECT * FROM product_marking WHERE product_marking_product_id = :productId")
    abstract fun getProductMarkingByProductId(productId: Long): Flow<ProductMarkingEntity?>

    @Query(value = "SELECT * FROM product_marking WHERE product_marking_product_id = :productId")
    abstract fun getProductMarkingsByProductId(productId: Long): Flow<List<ProductMarkingEntity>>

    @Query(value = "SELECT * FROM product_marking")
    abstract fun getAllProductMarkings(): Flow<List<ProductMarkingEntity>>

    @Query(value = "DELETE FROM product_marking WHERE product_marking_product_id = :productId")
    abstract fun deleteProductMarkingByProductId(productId: Long)

    @Query(value = "DELETE FROM product_marking WHERE product_marking_product_id = :productId AND product_marking = :marking")
    abstract fun deleteProductMarkingByProductId(productId: Long, marking: String)
}