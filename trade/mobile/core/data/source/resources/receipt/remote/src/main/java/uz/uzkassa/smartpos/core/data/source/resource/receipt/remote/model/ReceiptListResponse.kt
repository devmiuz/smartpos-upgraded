package uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.model

import kotlinx.serialization.*
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.utils.kserialization.decodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual

@Serializable
internal data class ReceiptListResponse(
    @SerialName("receipts")
    val responses: List<ReceiptResponse>
) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    private object ReceiptResponseListSerializer : KSerializer<List<ReceiptResponse>> {
        private val json: Json = Json.actual
        private val serializer: KSerializer<ReceiptResponse> = ReceiptResponse.serializer()

        override val descriptor = PrimitiveDescriptor(
            serialName = "ReceiptResponseListSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun deserialize(decoder: Decoder): List<ReceiptResponse> {
            val jsonElement: JsonElement = decoder.decodeJson()
            return if (jsonElement is JsonArray) json.fromJson(serializer.list, jsonElement)
            else listOf(json.fromJson(serializer, jsonElement))
        }

        override fun serialize(encoder: Encoder, value: List<ReceiptResponse>) {
            encoder.encode(serializer.list, value)
        }
    }
}