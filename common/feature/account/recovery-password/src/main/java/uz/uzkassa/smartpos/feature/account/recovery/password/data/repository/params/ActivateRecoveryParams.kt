package uz.uzkassa.smartpos.feature.account.recovery.password.data.repository.params

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

internal class ActivateRecoveryParams(val code: String) {
    private var phoneNumber: String by Delegates.notNull()
    private var newPassword: String by Delegates.notNull()

    fun asJsonElement(phoneNumber: String, newPassword: String): JsonElement {
        this.phoneNumber = phoneNumber
        this.newPassword = newPassword
        return asJsonElement(this)
    }

    companion object : SerializationStrategy<ActivateRecoveryParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "ActivatePasswordRecoveryParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: ActivateRecoveryParams) {
            val jsonObject: JsonObject = JsonObject.create(
                "login" to JsonPrimitive(value.phoneNumber),
                "key" to JsonPrimitive(value.code),
                "newPassword" to JsonPrimitive(value.newPassword)
            )

            encoder.encodeJson(jsonObject)
        }
    }
}