package uz.uzkassa.smartpos.feature.user.settings.data.data.repository.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.data.source.resource.fullname.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullName
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullNameResponse
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.core.utils.kserialization.json.create
import kotlin.properties.Delegates

internal data class ChangeUserDataParams(
    val fullName: FullName? = null
) {
    private var user: User by Delegates.notNull()

    fun asJsonElement(user: User): JsonElement {
        this.user = user
        return asJsonElement(this)
    }

    companion object : SerializationStrategy<ChangeUserDataParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "SaveUserParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        @Suppress("EXPERIMENTAL_API_USAGE")
        override fun serialize(encoder: Encoder, value: ChangeUserDataParams) {
            val json: Json = Json.actual
            val jsonObject: JsonObject = JsonObject.create(
                "branchId" to JsonPrimitive(value.user.branchId),
                "id" to JsonPrimitive(value.user.id),
                "login" to JsonPrimitive(value.user.phoneNumber),
                "fullName" to json.toJson(
                    serializer = FullNameResponse.serializer(),
                    value = (value.fullName ?: value.user.fullName).mapToResponse()
                )
            )

            encoder.encodeJson(jsonObject)
        }
    }
}