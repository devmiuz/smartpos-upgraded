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

internal class UpdateBillParams(
    val billId: String,
    val clientId: String? = null,
    val expiry: String? = null,
    val nfc: String? = null,
    val pan: String? = null,
    val phone: String? = null
) {

    fun asJsonElement(): JsonElement {
        return asJsonElement(this)
    }

    companion object : SerializationStrategy<UpdateBillParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "CreateBillParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        @Suppress("EXPERIMENTAL_API_USAGE")
        override fun serialize(encoder: Encoder, value: UpdateBillParams) {
            val jsonObject: JsonObject = JsonObject.create(
                "billId" to JsonPrimitive(value.billId),
                "clientId" to JsonPrimitive(value.clientId),
                "expiry" to JsonPrimitive(value.expiry),
                "nfc" to JsonPrimitive(value.nfc),
                "pan" to JsonPrimitive(value.pan),
                "phone" to JsonPrimitive(value.phone)
            )
            encoder.encodeJson(jsonObject)
        }
    }

}
