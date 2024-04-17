package uz.uzkassa.smartpos.core.data.source.resource.company.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyRelation
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao

@Dao
abstract class CompanyRelationDao : BaseDao {

    @Transaction
    @Query(
        value =
        "SELECT * " +
                "FROM companies " +
                "LEFT JOIN activity_types ON company_activity_types_ids = company_region_id " +
                "INNER JOIN company_business_types ON company_company_business_type_code = company_business_type_code " +
                "INNER JOIN cities ON company_city_id = city_id " +
                "INNER JOIN regions ON company_region_id = region_id"
    )
    abstract fun getRelationFlow(): Flow<CompanyRelation?>

    @Transaction
    @Query(
        value =
        "SELECT * " +
                "FROM companies " +
                "LEFT JOIN activity_types ON company_activity_types_ids = company_region_id " +
                "INNER JOIN company_business_types ON company_company_business_type_code = company_business_type_code " +
                "INNER JOIN cities ON company_city_id = city_id " +
                "INNER JOIN regions ON company_region_id = region_id"
    )
    abstract fun getRelation(): CompanyRelation


}