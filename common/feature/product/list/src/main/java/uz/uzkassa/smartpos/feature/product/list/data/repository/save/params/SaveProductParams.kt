package uz.uzkassa.smartpos.feature.product.list.data.repository.save.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryResponse
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.core.utils.kserialization.json.create
import uz.uzkassa.smartpos.core.utils.kserialization.json.toJson
import java.math.BigDecimal
import kotlin.properties.Delegates

internal data class SaveProductParams(
    val productId: Long,
    val salesPrice: BigDecimal?
) {
    private var branchId: Long by Delegates.notNull()

    fun asJsonElement(branchId: Long): JsonElement {
        this.branchId = branchId
        return asJsonElement(this)
    }

    companion object : SerializationStrategy<SaveProductParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "SaveProductParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        @Suppress("EXPERIMENTAL_API_USAGE")
        override fun serialize(encoder: Encoder, value: SaveProductParams) {
            val jsonObject: JsonObject = JsonObject.create(
                "branchId" to JsonPrimitive(value.branchId),
                "productId" to JsonPrimitive(value.productId),
                "salesPrice" to JsonPrimitive(value.salesPrice)
            )
            encoder.encodeJson(jsonObject)
        }
    }
}