package uz.uzkassa.apay.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerificationRequest(

        @SerialName("sms")
        val smsCode: String,

        @SerialName("transactionId")
        val transactionId: Int
)