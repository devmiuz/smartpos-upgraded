package uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.status

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectSerializer
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.content
import uz.uzkassa.smartpos.core.utils.kserialization.decodeJson

@Serializable(with = CashTransactionStatusRequest.Companion::class)
enum class CashTransactionStatusRequest {
    PAID,
    RETURNED,
    INCOME,
    WITHDRAW,
    FLOW,
    RETURN_FLOW,
    INCASSATION,
//    CREDIT,
//    ADVANCE,
    UNKNOWN;

    companion object : KSerializer<CashTransactionStatusRequest> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "CashTransactionStatusRequestSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun deserialize(decoder: Decoder): CashTransactionStatusRequest {
            
            val jsonObject: JsonObject = decoder.decodeJson().jsonObject
            val code: String = checkNotNull(jsonObject["code"]).content
            return runCatching { valueOf(code) }.getOrDefault(UNKNOWN)
        }

        override fun serialize(encoder: Encoder, value: CashTransactionStatusRequest) {
            val jsonObject = JsonObject(mapOf("code" to JsonPrimitive(value.name)))
            encoder.encode(JsonObjectSerializer, jsonObject)
        }
    }
}