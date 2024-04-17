package uz.uzkassa.smartpos.core.data.source.resource.branch.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.dao.ActivityTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.branch.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchEntity
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchResponse
import uz.uzkassa.smartpos.core.data.source.resource.city.dao.CityEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.city.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.database.SupportRoomDatabase
import uz.uzkassa.smartpos.core.data.source.resource.region.dao.RegionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.region.mapper.mapToEntity

@Dao
abstract class BranchEntityDao(private val database: SupportRoomDatabase) : BaseDao.Impl<BranchEntity>() {
    private val activityTypeDao = database.getDao<ActivityTypeEntityDao>()
    private val cityEntityDao = database.getDao<CityEntityDao>()
    private val regionEntityDao = database.getDao<RegionEntityDao>()

    @Query(value = "DELETE FROM branches WHERE branch_id = :branchId")
    abstract fun deleteByBranchId(branchId: Long)

    @Query(value = "DELETE FROM branches")
    abstract fun deleteAll()

    @Query(value = "DELETE FROM branches WHERE branch_id NOT IN (SELECT branch_id FROM branches WHERE branch_id IN (:branchIds))")
    abstract fun deleteAllExceptBranchIds(branchIds: LongArray)

    fun save(responses: List<BranchResponse>) {
        deleteAllExceptBranchIds(responses.map { it.id }.toLongArray())
        activityTypeDao.insertOrUpdate(responses.mapNotNull { it.activityTypeResponse?.mapToEntity() })
        cityEntityDao.insertOrUpdate(responses.mapNotNull { it.cityResponse?.mapToEntity() })
        regionEntityDao.insertOrUpdate(responses.mapNotNull { it.regionResponse?.mapToEntity() })
        insertOrUpdate(responses.map { it.mapToEntity() })
    }

    fun save(response: BranchResponse) {
        with(response) {
            activityTypeResponse?.let { activityTypeDao.insertOrUpdate(it.mapToEntity()) }
            cityResponse?.let { cityEntityDao.insertOrUpdate(it.mapToEntity()) }
            regionResponse?.let { regionEntityDao.insertOrUpdate(it.mapToEntity()) }
            return@with insertOrUpdate(mapToEntity())
        }
    }

    fun clearAllTables(){
        database.clearAllTables()
        database.clearAll()
    }
}