package uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeResponse
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityResponse
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionResponse

@Serializable
data class BranchResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("deleted")
    val isDeleted: Boolean,

    @SerialName("fiscal")
    val isFiscal: Boolean,

    @SerialName("activityTypeDTO")
    val activityTypeResponse: ActivityTypeResponse? = null,

    @SerialName("region")
    val regionResponse: RegionResponse?,

    @SerialName("city")
    val cityResponse: CityResponse?,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("address")
    val address: String?
)