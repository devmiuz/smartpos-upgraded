package uz.uzkassa.smartpos.feature.user.autoprint.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteReceiptUserResponse(
    @SerialName("id")
    val id: Long?,
    @SerialName("activated")
    val isActivated: Boolean?,
    @SerialName("dismissed")
    val isDismissed: Boolean?,
    @SerialName("owner")
    val isOwner: Boolean?,
    @SerialName("isSuperAdmin")
    val isSuperAdmin: Boolean?,
    @SerialName("started")
    val isStarted: Boolean?
)
