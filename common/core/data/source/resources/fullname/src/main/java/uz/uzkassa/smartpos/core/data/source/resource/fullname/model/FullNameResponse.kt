package uz.uzkassa.smartpos.core.data.source.resource.fullname.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FullNameResponse(
    @SerialName("firstName")
    val firstName: String,

    @SerialName("lastName")
    val lastName: String? = null,

    @SerialName("patronymic")
    val patronymic: String? = null
)