package uz.uzkassa.smartpos.core.data.source.resource.city.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CityResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("regionId")
    val regionId: Long,

    @SerialName("nameRu")
    val nameRu: String,

    @SerialName("nameUz")
    val nameUz: String
)