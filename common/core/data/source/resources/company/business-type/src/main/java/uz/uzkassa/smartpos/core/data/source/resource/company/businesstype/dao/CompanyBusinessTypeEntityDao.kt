package uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao

@Dao
abstract class CompanyBusinessTypeEntityDao : BaseDao.Impl<CompanyBusinessTypeEntity>() {

    @Query("SELECT * FROM company_business_types")
    abstract fun getEntities(): List<CompanyBusinessTypeEntity>

    @Query("DELETE FROM company_business_types")
    abstract fun deleteAll()
}