package uz.uzkassa.smartpos.core.data.source.resource.account.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("login")
    val phoneNumber: String,

    @SerialName("activated")
    val isActivated: Boolean,

    @SerialName("authorities")
    val userRoleCodes: List<String>
)