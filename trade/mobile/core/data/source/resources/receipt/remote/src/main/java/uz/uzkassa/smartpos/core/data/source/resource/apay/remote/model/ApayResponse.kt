package uz.uzkassa.smartpos.core.data.source.resource.apay.remote.model

import kotlinx.serialization.*
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.utils.kserialization.decodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual

@Serializable
internal data class ApayResponse(
    @SerialName("message")
    val message: String
)