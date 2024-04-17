package uz.uzkassa.smartpos.feature.category.saving.data.params

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

internal data class CreateCategoryParams(val categoryName: String) {
    private var branchId: Long by Delegates.notNull()
    private var categoryParentId: Long? = null

    fun asJsonElement(branchId: Long, categoryParentId: Long?): JsonElement {
        this.branchId = branchId
        this.categoryParentId = categoryParentId
        return asJsonElement(this)
    }

    companion object : SerializationStrategy<CreateCategoryParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "CreateCategoryParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: CreateCategoryParams) {
            val jsonObject: JsonObject = JsonObject.create(
                "branchId" to JsonPrimitive(value.branchId),
                "parentId" to JsonPrimitive(value.categoryParentId),
                "name" to JsonPrimitive(value.categoryName),
                "enabled" to JsonPrimitive(true)
            )

            encoder.encodeJson(jsonObject)
        }
    }
}