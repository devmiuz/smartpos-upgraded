package uz.uzkassa.smartpos.core.data.source.resource.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullNameResponse

@Serializable
data class UserResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("branchId")
    val branchId: Long,

    @SerialName("login")
    val phoneNumber: String,

    @SerialName("langKey")
    val languageKey: String? = null,

    @SerialName("activated")
    val isActivated: Boolean,

    @SerialName("blocked")
    val isBlocked: Boolean? = null,

    @SerialName("owner")
    val isOwner: Boolean,

    @SerialName("fullName")
    val fullName: FullNameResponse,

    @SerialName("authorities")
    val userRoleCodes: List<String>,

    @SerialName("dismissed")
    val isDismissed: Boolean
)