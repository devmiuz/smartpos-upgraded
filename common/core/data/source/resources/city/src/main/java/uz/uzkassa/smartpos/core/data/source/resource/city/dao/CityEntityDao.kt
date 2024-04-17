package uz.uzkassa.smartpos.core.data.source.resource.city.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityEntity
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao

@Dao
abstract class CityEntityDao : BaseDao.Impl<CityEntity>() {

    @Query(value = "SELECT * FROM cities WHERE city_region_id = :regionId")
    abstract fun getEntities(regionId: Long): List<CityEntity>

    @Query("DELETE FROM cities")
    abstract fun deleteAll()
}