package uz.uzkassa.apay.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class CreatePaymentRequest(
    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("amount")
    val paidAmount: BigDecimal,

    @SerialName("deviceSerialNumber")
    val serialNumber: String,

    @SerialName("expiry")
    val cardExpiryDate: String,

    @SerialName("pan")
    val cardNumber: String,

    @SerialName("tin")
    val tin: String
)