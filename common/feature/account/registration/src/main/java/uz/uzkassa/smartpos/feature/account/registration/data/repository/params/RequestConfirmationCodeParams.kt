package uz.uzkassa.smartpos.feature.account.registration.data.repository.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.create

internal data class RequestConfirmationCodeParams(val phoneNumber: String) {

    fun asJsonElement() =
        asJsonElement(this)

    companion object : SerializationStrategy<RequestConfirmationCodeParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "RequestRegistrationConfirmationParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: RequestConfirmationCodeParams) =
            encoder.encodeJson(JsonObject.create("login" to JsonPrimitive(value.phoneNumber)))
    }
}