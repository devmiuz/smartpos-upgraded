package uz.uzkassa.smartpos.feature.user.settings.password.data.repository.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.create

internal data class UserChangePasswordParams(val currentPassword: String, val newPassword: String) {

    fun asJsonElement() =
        asJsonElement(this)

    companion object : SerializationStrategy<UserChangePasswordParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "ChangePasswordParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: UserChangePasswordParams) {
            val jsonObject: JsonObject = JsonObject.create(
                "currentPassword" to JsonPrimitive(value.currentPassword),
                "newPassword" to JsonPrimitive(value.newPassword)
            )

            encoder.encodeJson(jsonObject)
        }
    }
}