package uz.uzkassa.smartpos.core.data.source.resource.product.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.FlowPreview
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.database.SupportRoomDatabase
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitRelation
import uz.uzkassa.smartpos.core.data.source.resource.product.utils.filter
import uz.uzkassa.smartpos.core.data.source.resource.product.utils.isAllowedForSale
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductRelation as Relation

@Dao
abstract class ProductRelationDao(database: SupportRoomDatabase) : BaseDao {
    private val productUnitRelationDao = database.getDao<ProductUnitRelationDao>()

    @Query(value = "SELECT * FROM products INNER JOIN categories ON product_category_id = category_id LEFT JOIN units ON product_unit_id = unit_id WHERE product_barcode LIKE '%' || :barcode || '%' AND product_is_deleted = 0 AND CASE WHEN :allowedForSale = 1 THEN product_sales_price IS NOT NULL ELSE :allowedForSale = 0 END ")
    internal abstract fun getProductRelationByBarcode(
        barcode: String,
        allowedForSale: Boolean = true
    ): Relation?

    @Query(value = "SELECT * FROM products INNER JOIN categories ON product_category_id = category_id LEFT JOIN units ON product_unit_id = unit_id WHERE product_id = :productId AND product_is_deleted = 0 ")
    internal abstract fun getProductRelationByProductId(productId: Long): Relation?

    @Query(value = "SELECT * FROM products INNER JOIN categories ON product_category_id = category_id LEFT JOIN units ON product_unit_id = unit_id WHERE product_is_deleted = 0 AND CASE WHEN :allowedForSale = 1 THEN product_sales_price IS NOT NULL ELSE :allowedForSale = 0 END ")
    internal abstract fun getProductRelations(allowedForSale: Boolean = true): List<Relation>

    @FlowPreview
    fun getRelationByBarcode(barcode: String, allowedForSale: Boolean): Relation? {
        val relation: Relation? =
            getProductRelationByBarcode(barcode, allowedForSale)
                ?.let { setProductUnitRelations(it) }

        return if (relation?.isAllowedForSale == true && allowedForSale) relation
        else null
    }

    @FlowPreview
    fun getRelationByProductId(productId: Long): Relation? {
        return getProductRelationByProductId(productId)
            ?.let { setProductUnitRelations(it) }
    }

    @FlowPreview
    fun getRelations(allowedForSale: Boolean): List<Relation> {
        return getProductRelations(allowedForSale)
            .filter(allowedForSale)
            .map { setProductUnitRelations(it) }
    }

    private fun setProductUnitRelations(relation: Relation): Relation {
        val relations: List<ProductUnitRelation> = when {
            relation.productEntity.productUnitIds.let { it != null && it.isNotEmpty() } ->
                productUnitRelationDao.getRelationsByProductId(relation.productEntity.id)
            else -> emptyList()
        }

        return relation.copy(productUnitRelations = relations)
    }
}