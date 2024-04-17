package uz.uzkassa.smartpos.core.data.source.resource.category.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryEntity
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryResponse
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao

@Dao
abstract class CategoryEntityDao : BaseDao.Impl<CategoryEntity>() {

    @Query(value = "SELECT * FROM categories WHERE category_id = :categoryId")
    internal abstract fun getEntityByCategoryId(categoryId: Long): CategoryEntity?

    @Query(value = "SELECT * FROM categories WHERE category_parent_id IS NULL AND CASE WHEN :onlyEnabled == 1 THEN category_enabled = :onlyEnabled END")
    internal abstract fun getParentEntities(onlyEnabled: Boolean = false): List<CategoryEntity>

    @Query(value = "SELECT * FROM categories WHERE category_parent_id = :parentId AND CASE WHEN :onlyEnabled == 1 THEN category_enabled = :onlyEnabled END")
    internal abstract fun getChildEntities(
        parentId: Long,
        onlyEnabled: Boolean = false
    ): List<CategoryEntity>

    @Query(value = "DELETE FROM categories")
    abstract fun deleteAll()

    fun save(responses: List<CategoryResponse>) {
        responses.forEach {
            upsert(it.mapToEntity())
            if (it.childCategories.isNotEmpty())
                save(it.childCategories)
        }
    }

    fun save(response: CategoryResponse) {
        upsert(response.mapToEntity())
        if (response.childCategories.isNotEmpty())
            save(response.childCategories)
    }
}