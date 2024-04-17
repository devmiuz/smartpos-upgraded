package uz.uzkassa.smartpos.core.data.source.resource.company.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.dao.ActivityTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.city.dao.CityEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.city.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.dao.CompanyBusinessTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyResponse
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.database.SupportRoomDatabase
import uz.uzkassa.smartpos.core.data.source.resource.region.dao.RegionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.region.mapper.mapToEntity

@Dao
abstract class CompanyEntityDao(database: SupportRoomDatabase) : BaseDao.Impl<CompanyEntity>() {
    private val activityTypeEntityDao = database.getDao<ActivityTypeEntityDao>()
    private val cityEntityDao = database.getDao<CityEntityDao>()
    private val companyBusinessTypeEntityDao = database.getDao<CompanyBusinessTypeEntityDao>()
    private val regionEntityDao = database.getDao<RegionEntityDao>()

    @Query(value = "DELETE FROM companies")
    abstract fun deleteAll()

    fun save(response: CompanyResponse) {
        deleteAll()
        with(response) {
            activityTypeEntityDao.insertOrUpdate(activityType.mapToEntities())
            companyBusinessTypeEntityDao.insertOrUpdate(companyBusinessType.mapToEntity())
            cityEntityDao.insertOrUpdate(city.mapToEntity())
            regionEntityDao.insertOrUpdate(region.mapToEntity())
            return@with insertOrUpdate(mapToEntity())
        }
    }
}