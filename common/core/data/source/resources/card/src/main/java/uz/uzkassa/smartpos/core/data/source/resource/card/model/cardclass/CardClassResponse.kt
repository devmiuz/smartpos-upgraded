package uz.uzkassa.smartpos.core.data.source.resource.card.model.cardclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardClassResponse(
    @SerialName("code")
    val type: String,

    @SerialName("nameRu")
    val nameRu: String
)