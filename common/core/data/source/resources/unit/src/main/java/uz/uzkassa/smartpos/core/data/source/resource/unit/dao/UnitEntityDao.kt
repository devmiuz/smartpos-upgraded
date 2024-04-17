package uz.uzkassa.smartpos.core.data.source.resource.unit.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitEntity

@Dao
abstract class UnitEntityDao : BaseDao.Impl<UnitEntity>() {

    @Query(value = "SELECT * FROM units")
    abstract fun getEntities(): List<UnitEntity>

    @Query(value = "DELETE FROM units")
    abstract fun deleteAll()
}