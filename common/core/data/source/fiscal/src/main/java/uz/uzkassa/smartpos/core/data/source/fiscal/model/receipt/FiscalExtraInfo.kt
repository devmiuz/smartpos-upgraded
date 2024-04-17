package uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt

import kotlinx.serialization.Serializable

@Serializable
data class FiscalExtraInfo(
    val carNumber: String = "",
    val other: String = "",
    val phoneNumber: String = "",
    val pinfl: String = "",
    val qrPaymentId: String,
    val qrPaymentProvider: Int,
    val tin: String = ""
)