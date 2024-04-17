package uz.uzkassa.smartpos.core.data.source.resource.activitytype.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityTypeResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("name")
    val name: String,

    @SerialName("parentId")
    val parentId: Long? = null
)