package uz.uzkassa.smartpos.feature.product.saving.data.repository.save.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryResponse
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.mapper.mapToResponses
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitResponse
import uz.uzkassa.smartpos.core.data.source.resource.unit.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitResponse
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.core.utils.kserialization.json.create
import uz.uzkassa.smartpos.core.utils.kserialization.json.toJson
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer
import java.math.BigDecimal
import kotlin.properties.Delegates

internal data class SaveProductParams(
    val productId: Long?,
    val isDeleted: Boolean,
    val isFavorite: Boolean,
    val barcode: String,
    val model: String? = null,
    val measurement: String? = null,
    val salesPrice: BigDecimal? = null,
    val hasMark: Boolean = false,
    val vatRate: BigDecimal? = null,
    val name: String,
    val category: Category?,
    val unit: Unit? = null,
    val productUnits: List<ProductUnit>? = null,
    val vatBarcode: String? = null,
    val committentTin: String? = null
) {
    private var branchId: Long by Delegates.notNull()

    constructor(
        barcode: String,
        model: String?,
        measurement: String?,
        salesPrice: BigDecimal?,
        hasMark: Boolean,
        vatRate: BigDecimal? = null,
        name: String,
        category: Category,
        unit: Unit?,
        productUnits: List<ProductUnit>?,
        vatBarcode: String?,
        committentTin: String?
    ) : this(
        productId = null,
        isDeleted = false,
        isFavorite = false,
        barcode = barcode,
        model = model,
        measurement = measurement,
        hasMark = hasMark,
        salesPrice = salesPrice,
        vatRate = vatRate,
        name = name,
        category = category,
        unit = unit,
        productUnits = productUnits,
        vatBarcode = vatBarcode,
        committentTin = committentTin
    )

    constructor(
        product: Product,
        isDeleted: Boolean? = null,
        isFavorite: Boolean? = null,
        barcode: String? = null,
        model: String? = null,
        measurement: String? = null,
        salesPrice: BigDecimal? = null,
        vatRate: BigDecimal? = null,
        hasMark: Boolean? = null,
        name: String? = null,
        category: Category? = null,
        unit: Unit? = null,
        productUnits: List<ProductUnit>,
        vatBarcode: String? = null,
        committentTin: String? = null
    ) : this(
        productId = product.id,
        isDeleted = isDeleted ?: product.isDeleted,
        isFavorite = isFavorite ?: product.isFavorite,
        barcode = barcode ?: product.barcode,
        hasMark = hasMark ?: product.hasMark,
        model = model ?: product.model,
        measurement = measurement ?: product.measurement,
        salesPrice = salesPrice ?: product.salesPrice,
        vatRate = vatRate ?: product.vatRate,
        name = name ?: product.name,
        category = category ?: product.category,
        unit = unit ?: product.unit,
        productUnits = if (productUnits.isNotEmpty()) productUnits else product.productUnits,
        vatBarcode = vatBarcode,
        committentTin = committentTin
    )

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
            val json: Json = Json.actual
            val jsonObject: JsonObject = JsonObject.create(
                "id" to JsonPrimitive(value.productId),
                "branchId" to JsonPrimitive(value.branchId),
                "deleted" to JsonPrimitive(value.isDeleted),
                "favorite" to JsonPrimitive(value.isFavorite),
//                "noVat" to JsonPrimitive(value.isNoVat),
                "hasMark" to JsonPrimitive(value.hasMark),
                "barcode" to JsonPrimitive(value.barcode),
                "vatBarcode" to JsonPrimitive(value.vatBarcode),
                "model" to JsonPrimitive(value.model),
                "measurement" to JsonPrimitive(value.measurement),
                "salesPrice" to json.toJson(BigDecimalSerializer.Nullable, value.salesPrice),
                "vatRate" to JsonPrimitive(value.vatRate),
                "name" to JsonPrimitive(value.name),
                "committentTin" to JsonPrimitive(value.committentTin),
                "categoryDTO" to json.toJson(
                    serializer = CategoryResponse.serializer(),
                    value = value.category?.copy(childCategories = listOf())?.mapToResponse()
                ),
                "unit" to json.toJson(
                    serializer = UnitResponse.serializer(),
                    value = value.unit?.mapToResponse()
                ),
                "units" to json.toJson(
                    serializer = ProductUnitResponse.serializer().list,
                    value = value.productUnits?.mapToResponses()
                )
            )

            encoder.encodeJson(jsonObject)
        }
    }
}