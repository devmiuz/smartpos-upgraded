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

internal data class FinishRecoveryParams(val newPassword: String) {
    private var password: String by Delegates.notNull()

    fun asJsonElement(password: String): JsonElement {
        this.password = password
        return asJsonElement(this)
    }

    companion object : SerializationStrategy<FinishRecoveryParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "FinishPasswordRecoveryParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: FinishRecoveryParams) {
            val jsonObject: JsonObject = JsonObject.create(
                "currentPassword" to JsonPrimitive(value.password),
                "newPassword" to JsonPrimitive(value.newPassword)
            )

            encoder.encodeJson(jsonObject)
        }
    }
}