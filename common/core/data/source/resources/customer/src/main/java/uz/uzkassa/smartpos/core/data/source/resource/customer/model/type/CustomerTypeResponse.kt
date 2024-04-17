package uz.uzkassa.smartpos.core.data.source.resource.customer.model.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomerTypeResponse(
    @SerialName("code")
    val type: String,

    @SerialName("nameRu")
    val nameRu: String
)