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

internal data class FinishRegistrationParams(val newPassword: String) {
    private var currentPassword: String by Delegates.notNull()

    fun asJsonElement(currentPassword: String): JsonElement {
        this.currentPassword = currentPassword
        return asJsonElement(this)
    }

    companion object : SerializationStrategy<FinishRegistrationParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "FinishRegistrationParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: FinishRegistrationParams) {
            val jsonObject: JsonObject = JsonObject.create(
                "currentPassword" to JsonPrimitive(value.currentPassword),
                "newPassword" to JsonPrimitive(value.newPassword)
            )

            encoder.encodeJson(jsonObject)
        }
    }
}