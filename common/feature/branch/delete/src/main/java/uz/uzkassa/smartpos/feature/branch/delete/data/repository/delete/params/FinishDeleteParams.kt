package uz.uzkassa.smartpos.feature.branch.delete.data.repository.delete.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.create

@Deprecated("")
internal data class FinishDeleteParams(
    val id: Long,
    val name: String,
    val confirmationCode: Long,
    val deleteReason: String
) {

    fun asJsonElement() =
        asJsonElement(this)

    companion object : SerializationStrategy<FinishDeleteParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "FinishBranchDeleteParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: FinishDeleteParams) {
            val jsonObject: JsonObject = JsonObject.create(
                "id" to JsonPrimitive(value.id),
                "name" to JsonPrimitive(value.name),
                "confirmationCode" to JsonPrimitive(value.confirmationCode),
                "reasonForDeletion" to JsonPrimitive(value.deleteReason)
            )

            encoder.encodeJson(jsonObject)
        }
    }
}