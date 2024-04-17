package uz.uzkassa.smartpos.core.data.source.resource.product.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryResponse
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitResponse
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitResponse
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class ProductResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("branchId")
    val branchId: Long?,

    @SerialName("deleted")
    val isDeleted: Boolean? = null,

    @SerialName("custom")
    val isCustom: Boolean,

    @SerialName("favorite")
    val isFavorite: Boolean? = false,

    @SerialName("noVat")
    val isNoVat: Boolean? = false,

    @SerialName("hasExcise")
    val hasExcise: Boolean,

    @SerialName("hasMark")
    val hasMark: Boolean? = false,

    @SerialName("barcode")
    val barcode: String? = null,

    @SerialName("vatBarcode")
    val vatBarcode: String? = null,

    @SerialName("code")
    val code: Int? = null,

    @SerialName("model")
    val model: String? = null,

    @SerialName("measurement")
    val measurement: String? = null,

    @SerialName("productCount")
    val count: Double? = null,

    @Serializable(with = BigDecimalSerializer.Nullable::class)
    @SerialName("exciseAmount")
    val exciseAmount: BigDecimal?,

    @Serializable(with = BigDecimalSerializer.Nullable::class)
    @SerialName("salesPrice")
    val salesPrice: BigDecimal? = null,

    @Serializable(with = BigDecimalSerializer.Nullable::class)
    @SerialName("vatRate")
    val vatRate: BigDecimal? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("productNameUz")
    val nameUz: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("createdBy")
    val createdBy: String? = null,

    @SerialName("createdDate")
    val createdDate: String? = null,

    @SerialName("lastModifiedBy")
    val lastModifiedBy: String? = null,

    @SerialName("lastModifiedDate")
    val lastModifiedDate: String? = null,

    @SerialName("service")
    val isService: Boolean? = null,

    @SerialName("categoryDTO")
    val category: CategoryResponse,

    @SerialName("unit")
    val unit: UnitResponse? = null,

    @SerialName("units")
    val productUnits: List<ProductUnitResponse>? = null,

    @SerialName("committentTin")
    val committentTin: String? = null,

    @SerialName("vatPercent")
    val vatPercent: Double? = null,

    @SerialName("label")
    val label: String? = null
)