package uz.uzkassa.apay.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApayBillDetail(
    @SerialName("id")
    val billId: String,
    @SerialName("time")
    val time: String,
    @SerialName("providerId")
    val providerId: Int?
)

@Serializable
data class ApayCreateBillResponse(
    @SerialName("statusCode")
    val statusCode: Int,
    @SerialName("data")
    val billDetail: ApayBillDetail
)


@Serializable
data class ApayUpdateBillResponse(
    @SerialName("billId")
    val billId: String,
    @SerialName("smsSent")
    val bilsmsSentlId: Boolean,
    @SerialName("tryPin")
    val tryPin: Boolean
)