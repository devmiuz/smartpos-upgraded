package uz.uzkassa.smartpos.core.data.source.resource.region.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionEntity

@Dao
abstract class RegionEntityDao : BaseDao.Impl<RegionEntity>() {

    @Query(value = "SELECT * FROM regions")
    abstract fun getEntities(): List<RegionEntity>

    @Query(value = "DELETE FROM regions")
    abstract fun deleteAll()
}