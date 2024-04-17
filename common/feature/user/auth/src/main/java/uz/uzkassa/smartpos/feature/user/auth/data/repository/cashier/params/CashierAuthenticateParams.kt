package uz.uzkassa.smartpos.feature.user.auth.data.repository.cashier.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.create

internal data class CashierAuthenticateParams(
    val userId: Long,
    val phoneNumber: String,
    val pinCode: String,
    val userName: String,
    val userRoleType: UserRole.Type
) {

    fun asJsonElement() =
        asJsonElement(this)

    companion object : SerializationStrategy<CashierAuthenticateParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "CashierAuthenticateParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: CashierAuthenticateParams) {
            val jsonObject: JsonObject = JsonObject.create(
                "username" to JsonPrimitive(value.phoneNumber),
                "password" to JsonPrimitive(value.pinCode),
                "userId" to JsonPrimitive(value.userId),
                "userRoleType" to JsonPrimitive(value.userRoleType.name)
            )

            encoder.encodeJson(jsonObject)
        }
    }
}