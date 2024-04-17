package uz.uzkassa.smartpos.core.data.source.resource.activitytype.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao

@Dao
abstract class ActivityTypeEntityDao : BaseDao.Impl<ActivityTypeEntity>() {

    @Query(value = "SELECT * FROM activity_types")
    abstract fun getEntities(): Flow<List<ActivityTypeEntity>>

    @Query(value = "SELECT * FROM activity_types WHERE activity_type_name LIKE '%' || :name || '%' ")
    abstract fun findEntitiesByName(name: String): Flow<List<ActivityTypeEntity>>
}