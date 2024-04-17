package uz.uzkassa.smartpos.core.data.source.resource.category.dao

import androidx.room.Dao
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryRelation
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.database.SupportRoomDatabase

@Dao
abstract class CategoryRelationDao(database: SupportRoomDatabase) : BaseDao {
    private val categoryEntityDao = database.getDao<CategoryEntityDao>()

    fun getRelationByCategoryId(categoryId: Long): CategoryRelation? =
        categoryEntityDao.getEntityByCategoryId(categoryId)
            ?.let { CategoryRelation(it, emptyList()) }

    fun getRelations(onlyEnabled: Boolean = false): List<CategoryRelation> =
        categoryEntityDao.getParentEntities()
            .map {
                val childRelations: List<CategoryRelation> =
                    if (it.parentId != null)
                        getChildRelations(checkNotNull(it.parentId), onlyEnabled)
                    else emptyList()

                return@map CategoryRelation(it, childRelations)
            }

    private fun getChildRelations(
        parentId: Long,
        onlyEnabled: Boolean
    ): List<CategoryRelation> =
        categoryEntityDao.getChildEntities(parentId, onlyEnabled)
            .map {
                val childRelations: List<CategoryRelation> =
                    if (it.parentId != null)
                        getChildRelations(checkNotNull(it.parentId), onlyEnabled)
                    else emptyList()
                return@map CategoryRelation(it, childRelations)
            }
}