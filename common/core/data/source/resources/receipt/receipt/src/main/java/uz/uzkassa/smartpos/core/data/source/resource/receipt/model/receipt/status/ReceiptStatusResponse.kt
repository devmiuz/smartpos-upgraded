package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectSerializer
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.content
import uz.uzkassa.smartpos.core.utils.kserialization.decodeJson

@Serializable(with = ReceiptStatusResponse.Companion::class)
enum class ReceiptStatusResponse {
    DELETED,
    DRAFT,
    PAID,
    RETURNED,
    CREDIT,
    ADVANCE,
    UNKNOWN;

    companion object : KSerializer<ReceiptStatusResponse> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "ReceiptStatusResponseSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun deserialize(decoder: Decoder): ReceiptStatusResponse {
            val jsonObject: JsonObject = decoder.decodeJson().jsonObject
            val code: String = checkNotNull(jsonObject["code"]).content
            return runCatching { valueOf(code) }.getOrDefault(UNKNOWN)
        }

        override fun serialize(encoder: Encoder, value: ReceiptStatusResponse) {
            val jsonObject = JsonObject(mapOf("code" to JsonPrimitive(value.name)))
            encoder.encode(JsonObjectSerializer, jsonObject)
        }
    }
}