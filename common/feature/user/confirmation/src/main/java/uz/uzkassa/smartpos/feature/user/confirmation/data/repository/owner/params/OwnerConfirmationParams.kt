package uz.uzkassa.smartpos.feature.user.confirmation.data.repository.owner.params

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

internal data class OwnerConfirmationParams(val password: String) {
    private var phoneNumber: String by Delegates.notNull()

    fun asJsonElement(phoneNumber: String): JsonElement {
        this.phoneNumber = phoneNumber
        return asJsonElement(this)
    }

    internal companion object : SerializationStrategy<OwnerConfirmationParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "OwnerConfirmationParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: OwnerConfirmationParams) {
            val jsonObject: JsonObject = JsonObject.create(
                "username" to JsonPrimitive(value.phoneNumber),
                "password" to JsonPrimitive(value.password)
            )

            encoder.encodeJson(jsonObject)
        }
    }
}