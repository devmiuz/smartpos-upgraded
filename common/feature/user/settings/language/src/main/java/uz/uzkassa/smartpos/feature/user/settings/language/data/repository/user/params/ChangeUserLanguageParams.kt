package uz.uzkassa.smartpos.feature.user.settings.language.data.repository.user.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.*
import uz.uzkassa.smartpos.core.data.source.resource.fullname.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullNameResponse
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.core.utils.kserialization.json.create
import kotlin.properties.Delegates

internal data class ChangeUserLanguageParams(val language: Language) {
    private var user: User by Delegates.notNull()

    fun asJsonElement(user: User): JsonElement {
        this.user = user
        return asJsonElement(this)
    }


    companion object : SerializationStrategy<ChangeUserLanguageParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "SaveUserParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        @Suppress("EXPERIMENTAL_API_USAGE")
        override fun serialize(encoder: Encoder, value: ChangeUserLanguageParams) {
            val json: Json = Json.actual
            val jsonObject: JsonObject = JsonObject.create(
                "branchId" to JsonPrimitive(value.user.branchId),
                "id" to JsonPrimitive(value.user.id),
                "login" to JsonPrimitive(value.user.phoneNumber),
                "langKey" to JsonPrimitive(value.language.languageKey.toLowerCase()),
                "fullName" to json.toJson(
                    serializer = FullNameResponse.serializer(),
                    value = (value.user.fullName).mapToResponse()
                ),
                "activated" to JsonPrimitive(value.user.isActivated),
                "blocked" to JsonPrimitive(value.user.isBlocked),
                "owner" to JsonPrimitive(value.user.isOwner),
                "dismissed" to JsonPrimitive(value.user.isDismissed),
                "authorities" to JsonArray(
                    value.user.userRoleCodes.toList().map { JsonPrimitive(it) })
            )

            encoder.encodeJson(jsonObject)
        }
    }
}