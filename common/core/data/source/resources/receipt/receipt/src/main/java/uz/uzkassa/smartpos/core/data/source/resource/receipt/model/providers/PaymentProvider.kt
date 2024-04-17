package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.providers

import kotlinx.serialization.Serializable

@Serializable
data class PaymentProvider(
    val ADDRESS: String,
    val ID: Int,
    val NAME: String,
    val NS10_CODE: Int,
    val NS11_CODE: Int,
    val STATUS: Int,
    val TIN: String,
    val UNI_CODE: String
)