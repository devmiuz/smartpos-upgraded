package uz.uzkassa.apay.data.repository.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.create
import java.math.BigDecimal

internal class CreateBillParams(
    val amount: BigDecimal,
    val cardId: String? = null,
    val ctoId: Long? = null,
    val expiry: String? = null,
    val pan: String? = null,
    val uniqueId: String? = null
) {

    fun asJsonElement(): JsonElement {
        return asJsonElement(this)
    }

    companion object : SerializationStrategy<CreateBillParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "CreateBillParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        @Suppress("EXPERIMENTAL_API_USAGE")
        override fun serialize(encoder: Encoder, value: CreateBillParams) {
            val jsonObject: JsonObject = JsonObject.create(
                "amount" to JsonPrimitive(value.amount),
                "cardId" to JsonPrimitive(value.cardId),
                "ctoId" to JsonPrimitive(value.ctoId),
                "expiry" to JsonPrimitive(value.expiry),
                "pan" to JsonPrimitive(value.pan),
                "uniqueId" to JsonPrimitive(value.uniqueId)
            )
            encoder.encodeJson(jsonObject)
        }
    }

}
