package uz.uzkassa.smartpos.core.data.source.resource.company.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "companies")
data class CompanyEntity(
    @PrimaryKey
    @ColumnInfo(name = "company_id")
    val id: Long,

    @ColumnInfo(name = "company_created_date")
    val createdDate: Date? = null,

    @ColumnInfo(name = "company_activity_types_ids")
    val activityTypeIds: LongArray,

    @ColumnInfo(name = "company_company_business_type_code")
    val companyBusinessTypeCode: String,

    @ColumnInfo(name = "company_city_id")
    val cityId: Long,

    @ColumnInfo(name = "company_region_id")
    val regionId: Long,

    @ColumnInfo(name = "company_is_warehouse_enabled")
    val isWarehouseEnabled: Boolean,

    @ColumnInfo(name = "company_is_pays_vat")
    val isPaysVat: Boolean,

    @ColumnInfo(name = "company_is_fiscal")
    val isFiscal: Boolean,

    @ColumnInfo(name = "company_vat_percent")
    val vatPercent: Double?,

    @ColumnInfo(name = "company_tin")
    val tin: Long?,

    @ColumnInfo(name = "company_name")
    val name: String,

    @ColumnInfo(name = "company_address")
    val address: String,

    @ColumnInfo(name = "payment_types")
    val paymentTypes: Array<String>?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompanyEntity

        if (id != other.id) return false
        if (!activityTypeIds.contentEquals(other.activityTypeIds)) return false
        if (companyBusinessTypeCode != other.companyBusinessTypeCode) return false
        if (cityId != other.cityId) return false
        if (regionId != other.regionId) return false
        if (isWarehouseEnabled != other.isWarehouseEnabled) return false
        if (isPaysVat != other.isPaysVat) return false
        if (isFiscal != other.isFiscal) return false
        if (vatPercent != other.vatPercent) return false
        if (tin != other.tin) return false
        if (name != other.name) return false
        if (address != other.address) return false
        if (paymentTypes != null) {
            if (other.paymentTypes == null) return false
            if (!paymentTypes.contentEquals(other.paymentTypes)) return false
        } else if (other.paymentTypes != null) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + activityTypeIds.contentHashCode()
        result = 31 * result + companyBusinessTypeCode.hashCode()
        result = 31 * result + cityId.hashCode()
        result = 31 * result + regionId.hashCode()
        result = 31 * result + isWarehouseEnabled.hashCode()
        result = 31 * result + isPaysVat.hashCode()
        result = 31 * result + isFiscal.hashCode()
        result = 31 * result + vatPercent.hashCode()
        result = 31 * result + (tin?.hashCode() ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + (paymentTypes?.contentHashCode() ?: 0)
        return result
    }
}