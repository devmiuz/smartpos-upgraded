package uz.uzkassa.smartpos.core.data.source.resource.product.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.FlowPreview
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.database.SupportRoomDatabase
import uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination.ProductPaginationRelation
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitRelation
import uz.uzkassa.smartpos.core.data.source.resource.product.utils.filter
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductRelation as Relation

@Dao
abstract class ProductPaginationRelationDao(database: SupportRoomDatabase) : BaseDao {
    private val productUnitRelationDao = database.getDao<ProductUnitRelationDao>()

    @Query(value = "SELECT COUNT(product_id) as size, (COUNT(product_id) / :limit) as pages, (CASE WHEN :offset = 0 THEN 1 ELSE 0 END) as isFirst, (CASE WHEN :limit + :offset >= COUNT(product_id) THEN 1 ELSE 0 END) as isLast FROM products WHERE product_category_id = :categoryId AND product_is_deleted = 0")
    internal abstract fun getProductPaginationRelationByCategoryId(
        categoryId: Long,
        limit: Int,
        offset: Int
    ): ProductPaginationRelation

    @Query(value = "SELECT COUNT(product_id) as size, (COUNT(product_id) / :limit) as pages, (CASE WHEN :offset = 0 THEN 1 ELSE 0 END) as isFirst, (CASE WHEN :limit + :offset >= COUNT(product_id) THEN 1 ELSE 0 END) as isLast FROM products WHERE product_is_favorite = 1 AND product_is_deleted = 0")
    internal abstract fun getProductPaginationRelationByFavorites(
        limit: Int,
        offset: Int
    ): ProductPaginationRelation

    @Query(value = "SELECT COUNT(product_id) as size, (COUNT(product_id) / :limit) as pages, (CASE WHEN :offset = 0 THEN 1 ELSE 0 END) as isFirst, (CASE WHEN :limit + :offset >= COUNT(product_id) THEN 1 ELSE 0 END) as isLast FROM products WHERE product_name LIKE '%' || :productName || '%' AND product_is_deleted = 0")
    internal abstract fun getProductPaginationRelationByProductName(
        productName: String,
        limit: Int,
        offset: Int
    ): ProductPaginationRelation

    @Query(value = "SELECT * FROM products INNER JOIN categories ON product_category_id = category_id LEFT JOIN units ON product_unit_id = unit_id WHERE product_category_id = :categoryId AND product_is_deleted = 0 AND CASE WHEN :isAllowedForSale = 1 THEN product_sales_price IS NOT NULL ELSE :isAllowedForSale = 0 END LIMIT :limit OFFSET :offset ")
    internal abstract fun getProductRelationsByCategoryId(
        categoryId: Long,
        limit: Int,
        offset: Int,
        isAllowedForSale: Boolean = true
    ): List<Relation>

    @Query(value = "SELECT * FROM products INNER JOIN categories ON product_category_id = category_id LEFT JOIN units ON product_unit_id = unit_id WHERE product_name LIKE '%' || :name || '%' AND product_is_deleted = 0 AND CASE WHEN :isAllowedForSale = 1 THEN product_sales_price IS NOT NULL ELSE :isAllowedForSale = 0 END LIMIT :limit OFFSET :offset ")
    internal abstract fun getProductRelationsByName(
        name: String,
        limit: Int,
        offset: Int,
        isAllowedForSale: Boolean = true
    ): List<Relation>

    @Query(value = "SELECT * FROM products INNER JOIN categories ON product_category_id = category_id LEFT JOIN units ON product_unit_id = unit_id WHERE product_is_favorite = 1 AND product_is_deleted = 0 AND CASE WHEN :isAllowedForSale = 1 THEN product_sales_price IS NOT NULL ELSE :isAllowedForSale = 0 END  LIMIT :limit OFFSET :offset ")
    internal abstract fun getProductRelationsByFavorites(
        limit: Int,
        offset: Int,
        isAllowedForSale: Boolean = true
    ): List<Relation>

    @Query(value = "SELECT COUNT(product_id) as size, (COUNT(product_id) / :limit) as pages, (CASE WHEN :offset = 0 THEN 1 ELSE 0 END) as isFirst, (CASE WHEN :limit + :offset >= COUNT(product_id) THEN 1 ELSE 0 END) as isLast FROM products")
    abstract fun getAllProducts(
        limit: Int,
        offset: Int
    ): ProductPaginationRelation

    @Query(value = "SELECT * FROM products INNER JOIN categories ON product_category_id = category_id LEFT JOIN units ON product_unit_id = unit_id LIMIT :limit OFFSET :offset ")
    abstract fun getProductRelations(
        limit: Int,
        offset: Int
    ): List<Relation>


    @FlowPreview
    fun getRelationsByCategoryId(
        categoryId: Long,
        page: Int,
        size: Int,
        isAllowedForSale: Boolean = true
    ): ProductPaginationRelation {
        val offset: Int = size * (page - 1)
        val paginationRelation: ProductPaginationRelation =
            getProductPaginationRelationByCategoryId(categoryId, size, offset)

        val relations: List<Relation> =
            getProductRelationsByCategoryId(categoryId, size, offset, isAllowedForSale)
                .filter(isAllowedForSale)
                .map { setProductUnitRelations(it) }

        return paginationRelation.copy(currentPage = page, productRelations = relations)
    }

    @FlowPreview
    fun getRelationsByName(
        name: String,
        page: Int,
        size: Int,
        isAllowedForSale: Boolean = true
    ): ProductPaginationRelation {
        val offset: Int = size * (page - 1)
        val paginationRelation: ProductPaginationRelation =
            getProductPaginationRelationByProductName(name, size, offset)

        val relations: List<Relation> =
            getProductRelationsByName(name, size, offset, isAllowedForSale)
                .filter(isAllowedForSale)
                .map { setProductUnitRelations(it) }

        return paginationRelation.copy(currentPage = page, productRelations = relations)
    }

    @FlowPreview
    fun getRelationsByFavorites(
        page: Int,
        size: Int,
        isAllowedForSale: Boolean = true
    ): ProductPaginationRelation {
        val offset: Int = size * (page - 1)
        val paginationRelation: ProductPaginationRelation =
            getProductPaginationRelationByFavorites(size, offset)

        val relations: List<Relation> =
            getProductRelationsByFavorites(size, offset, isAllowedForSale)
                .filter(isAllowedForSale)
                .map { setProductUnitRelations(it) }

        return paginationRelation.copy(currentPage = page, productRelations = relations)
    }
//
//    @FlowPreview
//    fun getRelationsAllProducts(
//        page: Int,
//        size: Int
//    ): ProductPaginationRelation {
//        val offset: Int = size * (page - 1)
//        val paginationRelation: ProductPaginationRelation =
//            getAllProducts(size, offset)
//
//        val relations: List<uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductRelation> =
//            getProductRelations(size, offset)
//                .map { setProductUnitRelations(it) }
//        return paginationRelation.copy(currentPage = page, productRelations = relations)
//    }

    private fun setProductUnitRelations(relation: Relation): Relation {
        val relations: List<ProductUnitRelation> = when {
            relation.productEntity.productUnitIds.let { it != null && it.isNotEmpty() } ->
                productUnitRelationDao.getRelationsByProductId(relation.productEntity.id)
            else -> emptyList()
        }

        return relation.copy(productUnitRelations = relations)
    }
}