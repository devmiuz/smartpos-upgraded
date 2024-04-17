package uz.uzkassa.smartpos.feature.user.auth.data.repository.cashier.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.create

internal data class CashierRequestNewPinCodeParams(val phoneNumber: String) {

    fun asJsonElement() =
        asJsonElement(this)

    companion object : SerializationStrategy<CashierRequestNewPinCodeParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "CashierRequestNewPinCodeParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: CashierRequestNewPinCodeParams) =
            encoder.encodeJson(JsonObject.create("login" to JsonPrimitive(value.phoneNumber)))
    }
}