package uz.uzkassa.smartpos.feature.category.selection.setup.data.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.mapToResponses
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryResponse
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson

internal data class EnabledCategoriesParams(val categories: List<Category>) {

    fun asJsonElement() =
        asJsonElement(this)

    companion object : SerializationStrategy<EnabledCategoriesParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "CategoriesParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        @Suppress("EXPERIMENTAL_API_USAGE")
        override fun serialize(encoder: Encoder, value: EnabledCategoriesParams) {
            val jsonElement: JsonElement = Json.toJson(
                serializer = ListSerializer(CategoryResponse.serializer()),
                value = value.categories.mapToResponses()
            )

            encoder.encodeJson(jsonElement)
        }
    }
}