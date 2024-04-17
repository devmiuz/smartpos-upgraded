package uz.uzkassa.smartpos.core.data.source.resource.unit.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnitResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("code")
    val code: Long,

    @SerialName("countable")
    val isCountable: Boolean,

    @SerialName("name")
    val name: String,

    @SerialName("nameRu")
    val nameRu: String? = null,

    @SerialName("nameUz")
    val nameUz: String,

    @SerialName("description")
    val description: String
)