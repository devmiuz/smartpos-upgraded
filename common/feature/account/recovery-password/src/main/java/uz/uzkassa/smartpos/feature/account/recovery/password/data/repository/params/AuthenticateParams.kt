package uz.uzkassa.smartpos.feature.account.recovery.password.data.repository.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.create

internal data class AuthenticateParams(val phoneNumber: String, val password: String) {

    fun asJsonElement() =
        asJsonElement(this)

    companion object : SerializationStrategy<AuthenticateParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "AuthenticateParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: AuthenticateParams) {
            val jsonObject: JsonObject = JsonObject.create(
                "username" to JsonPrimitive(value.phoneNumber),
                "password" to JsonPrimitive(value.password)
            )

            encoder.encodeJson(jsonObject)
        }
    }
}