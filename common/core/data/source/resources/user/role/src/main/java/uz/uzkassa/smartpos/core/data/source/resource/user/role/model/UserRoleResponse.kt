package uz.uzkassa.smartpos.core.data.source.resource.user.role.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRoleResponse(
    @SerialName("roleCode")
    val code: String,

    @SerialName("code")
    val type: String,

    @SerialName("priority")
    val priority: Int,

    @SerialName("nameRu")
    val nameRu: String
)