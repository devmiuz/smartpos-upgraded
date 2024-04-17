package uz.uzkassa.smartpos.feature.account.registration.data.repository.params

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
import kotlin.properties.Delegates

internal data class ActivateRegistrationParams(val phoneNumber: String, val code: String) {
    private var newPassword: String by Delegates.notNull()

    fun asJsonElement(newPassword: String): JsonElement {
        this.newPassword = newPassword
        return asJsonElement(this)
    }

    companion object : SerializationStrategy<ActivateRegistrationParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "ActivatePasswordRecoveryParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: ActivateRegistrationParams) {
            val jsonObject: JsonObject = JsonObject.create(
                    "login" to JsonPrimitive(value.phoneNumber),
                    "key" to JsonPrimitive(value.code),
                    "newPassword" to JsonPrimitive(value.newPassword)
                )

            encoder.encodeJson(jsonObject)
        }
    }
}