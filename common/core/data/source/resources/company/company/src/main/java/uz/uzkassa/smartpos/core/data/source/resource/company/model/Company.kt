package uz.uzkassa.smartpos.core.data.source.resource.company.model

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import java.util.*

data class Company(
    val id: Long,
    val createdDate: Date?,
    val activityType: List<ActivityType>,
    val companyBusinessType: CompanyBusinessType,
    val city: City,
    val region: Region,
    val isWarehouseEnabled: Boolean,
    val isPaysVat: Boolean,
    val isFiscal: Boolean,
    val vatPercent: Double?,
    val tin: Long?,
    val name: String,
    val address: String,
    val paymentTypes: Array<String>?
)