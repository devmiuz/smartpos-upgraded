package uz.uzkassa.smartpos.core.data.source.resource.region.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegionResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("nameRu")
    val nameRu: String,

    @SerialName("nameUz")
    val nameUz: String
)