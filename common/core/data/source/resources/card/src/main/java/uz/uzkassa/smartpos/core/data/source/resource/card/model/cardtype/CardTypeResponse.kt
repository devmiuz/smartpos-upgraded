package uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardclass.CardClassResponse
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.DateSerializer
import java.util.*

@Serializable
data class CardTypeResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("createdById")
    val createdById: Long,

    @SerialName("updatedById")
    val updatedById: Long? = null,

    @SerialName("discountPercent")
    val discountPercent: Double,

    @SerialName("cardClass")
    val cardClass: CardClassResponse,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String? = null,

    @Serializable(with = DateSerializer.NotNullable::class)
    @SerialName("createdDateTime")
    val createdDate: Date,

    @Serializable(with = DateSerializer.Nullable::class)
    @SerialName("updatedDateTime")
    val updatedDate: Date? = null
)