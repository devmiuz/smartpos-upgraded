package uz.uzkassa.apay.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PayedResponse(
    val code: String,
    val name: String
)