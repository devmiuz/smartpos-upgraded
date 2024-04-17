package uz.uzkassa.smartpos.core.data.source.resource.company.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeResponse
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityResponse
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeResponse
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionResponse
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.DateSerializer
import java.util.*

@Serializable
data class CompanyResponse(
    @SerialName("id")
    val id: Long,

    @Serializable(DateSerializer.NotNullable::class)
    @SerialName("createdDate")
    val createdDate: Date?,

    @SerialName("types")
    val activityType: List<ActivityTypeResponse> = listOf(),

    @SerialName("businessType")
    val companyBusinessType: CompanyBusinessTypeResponse,

    @SerialName("city")
    val city: CityResponse,

    @SerialName("region")
    val region: RegionResponse,

    @SerialName("warehouseEnabled")
    val isWarehouseEnabled: Boolean,

    @SerialName("paysNds")
    val isPaysVat: Boolean,

    @SerialName("fiscal")
    val isFiscal: Boolean,

    @SerialName("ndsPercent")
    val vatPercent: Double?,

    @SerialName("inn")
    val tin: Long? = null,

    @SerialName("name")
    val name: String,

    @SerialName("address")
    val address: String,

    @SerialName("paymentTypes")
    val paymentTypes: Array<String>?
)