package uz.uzkassa.apay.data.model.pay_bill

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PayBillResponse(
    @SerialName("billId")
    val billId: String,
    @SerialName("status")
    val status: Status
)

@Serializable
data class Status(
    @SerialName("code")
    val code: String,
    @SerialName("name")
    val name: String
)
