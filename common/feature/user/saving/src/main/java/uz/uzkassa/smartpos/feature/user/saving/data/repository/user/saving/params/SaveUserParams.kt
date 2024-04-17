package uz.uzkassa.smartpos.feature.user.saving.data.repository.user.saving.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.data.source.resource.fullname.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullName
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullNameResponse
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.core.utils.kserialization.json.create
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.DateSerializer
import java.util.*

internal data class SaveUserParams(
    val id: Long? = null,
    val branchId: Long,
    val phoneNumber: String,
    val language: Language? = null,
    val userRole: UserRole,
    val startDate: Date? = null,
    val fullName: FullName
) {

    constructor(
        branchId: Long,
        phoneNumber: String,
        userRole: UserRole,
        startDate: Date,
        fullName: FullName
    ) : this(
        id = null,
        branchId = branchId,
        phoneNumber = phoneNumber,
        language = null,
        userRole = userRole,
        startDate = startDate,
        fullName = fullName
    )

    fun asJsonElement() =
        asJsonElement(this)


    companion object : SerializationStrategy<SaveUserParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "SaveUserParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        @Suppress("EXPERIMENTAL_API_USAGE")
        override fun serialize(encoder: Encoder, value: SaveUserParams) {
            val json: Json = Json.actual
            val jsonObject: JsonObject = JsonObject.create(
                "id" to JsonPrimitive(value.id),
                "branchId" to JsonPrimitive(value.branchId),
                "login" to JsonPrimitive(value.phoneNumber),
                "langKey" to JsonPrimitive(value.language?.locale?.language),
                "dismissed" to JsonPrimitive(false),
                "startDate" to json.toJson(DateSerializer.Nullable(), value.startDate),
                "authorities" to JsonArray(listOf(JsonPrimitive(value.userRole.code.name))),
                "fullName" to json.toJson(
                    serializer = FullNameResponse.serializer(),
                    value = value.fullName.mapToResponse()
                )
            )
            encoder.encodeJson(jsonObject)
        }
    }
}