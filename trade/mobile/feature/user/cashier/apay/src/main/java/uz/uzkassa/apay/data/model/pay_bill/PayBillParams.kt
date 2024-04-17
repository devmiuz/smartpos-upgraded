package uz.uzkassa.apay.data.model.pay_bill

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


internal class PayBillParams(
    val billId: String,
    val cardId: String?,
    val confirmationKey: String?
) {

    fun asJsonElement(): JsonElement {
        return asJsonElement(this)
    }

    companion object : SerializationStrategy<PayBillParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "CreateBillParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        @Suppress("EXPERIMENTAL_API_USAGE")
        override fun serialize(encoder: Encoder, value: PayBillParams) {
            val jsonObject: JsonObject = JsonObject.create(
                "billId" to JsonPrimitive(value.billId),
                "cardId" to JsonPrimitive(value.cardId),
                "confirmationKey" to JsonPrimitive(value.confirmationKey)
            )
            encoder.encodeJson(jsonObject)
        }
    }

}
