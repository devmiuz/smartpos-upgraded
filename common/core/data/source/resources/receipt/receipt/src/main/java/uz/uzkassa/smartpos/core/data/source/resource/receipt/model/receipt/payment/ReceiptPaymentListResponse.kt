package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import uz.uzkassa.smartpos.core.utils.enums.EnumCompanion
import uz.uzkassa.smartpos.core.utils.enums.valueOrDefault
import uz.uzkassa.smartpos.core.utils.kserialization.decodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import java.math.BigDecimal
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer.NotNullable as BigDecimalSerializer

@Serializable(with = ReceiptPaymentListResponse.Companion::class)
data class ReceiptPaymentListResponse internal constructor(
    val responses: List<ReceiptPaymentResponse>
) {

    data class ReceiptPaymentResponse(val amount: BigDecimal, val type: Type) {

        @Suppress("SpellCheckingInspection")
        enum class Type {
            CARD,
            CASH,
            APAY,
            HUMO,
            LOYALTY_CARD,
            OTHER,
            UZCARD;

            companion object : EnumCompanion<Type> {
                override val DEFAULT: Type = OTHER
            }
        }
    }

    companion object : KSerializer<ReceiptPaymentListResponse> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "ReceiptPaymentResponseSerializer",
            kind = PrimitiveKind.STRING
        )

        @Suppress("EXPERIMENTAL_API_USAGE")
        override fun deserialize(decoder: Decoder): ReceiptPaymentListResponse {
            val json: Json = Json.actual
            val jsonElement: JsonElement = decoder.decodeJson()
            if (jsonElement !is JsonObject) return ReceiptPaymentListResponse(emptyList())
            val responses: List<ReceiptPaymentResponse> =
                jsonElement.content.map {
                    val type: ReceiptPaymentResponse.Type =
                        ReceiptPaymentResponse.Type.valueOrDefault(it.key)
                    val bigDecimal: BigDecimal = json.fromJson(BigDecimalSerializer, it.value)
                    return@map ReceiptPaymentResponse(bigDecimal, type)
                }

            return ReceiptPaymentListResponse(responses)
        }

        override fun serialize(encoder: Encoder, value: ReceiptPaymentListResponse) {
            val map: Map<String, JsonElement> =
                value.responses.map { Pair(it.type.name, JsonPrimitive(it.amount)) }.toMap()
            encoder.encode(JsonObjectSerializer, JsonObject(map))
        }
    }
}