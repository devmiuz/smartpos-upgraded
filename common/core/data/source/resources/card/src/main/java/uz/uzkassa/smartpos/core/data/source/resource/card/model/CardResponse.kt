package uz.uzkassa.smartpos.core.data.source.resource.card.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype.CardTypeResponse
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.CustomerResponse
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.DateSerializer
import java.math.BigDecimal
import java.util.*

@Serializable
data class CardResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("activatedByUserId")
    val activatedByUserId: Long? = null,

    @SerialName("createdById")
    val createdById: Long,

    @SerialName("updatedById")
    val updatedById: Long? = null,

    @SerialName("issuedByBranchId")
    val issuedByBranchId: Long? = null,

    @SerialName("activated")
    val isActivated: Boolean,

    @SerialName("blocked")
    val isBlocked: Boolean,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("currBalance")
    val balance: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalDiscount")
    val totalDiscount: BigDecimal,

    @SerialName("cardNumber")
    val cardNumber: String,

    @SerialName("cardType")
    val cardType: CardTypeResponse,

    @SerialName("customer")
    val customer: CustomerResponse? = null,

    @Serializable(with = DateSerializer.Nullable::class)
    @SerialName("activatedDateTime")
    val activatedDate: Date? = null,

    @Serializable(with = DateSerializer.NotNullable::class)
    @SerialName("createdDateTime")
    val createdDate: Date,

    @Serializable(with = DateSerializer.Nullable::class)
    @SerialName("updatedDateTime")
    val updatedDate: Date? = null
)