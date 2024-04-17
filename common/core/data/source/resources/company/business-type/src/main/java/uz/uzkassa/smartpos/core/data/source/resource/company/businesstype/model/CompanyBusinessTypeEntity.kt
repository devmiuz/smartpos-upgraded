package uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_business_types")
data class CompanyBusinessTypeEntity(
    @PrimaryKey
    @ColumnInfo(name = "company_business_type_code")
    val code: String,

    @ColumnInfo(name = "company_business_type_name_ru")
    val nameRu: String
)