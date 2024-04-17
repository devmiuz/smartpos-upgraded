package uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitRelation

@Dao
abstract class ProductUnitRelationDao : BaseDao {

    @Query(value = "SELECT * FROM product_units INNER JOIN (SELECT * FROM units) unit ON unit_id = product_unit_unit_id WHERE product_unit_product_id = :productId AND product_unit_unit_id = :unitId")
    abstract fun getRelationByUnitId(productId: Long, unitId: Long): ProductUnitRelation?

    @Query(value = "SELECT * FROM product_units INNER JOIN (SELECT * FROM units) unit ON unit_id = product_unit_unit_id WHERE product_unit_product_id = :productId")
    abstract fun getRelationsByProductId(productId: Long): List<ProductUnitRelation>
}