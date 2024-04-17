package uz.uzkassa.smartpos.core.data.source.resource.product.unit.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class ProductUnitResponse(
    @SerialName("id")
    val id: Long?,

    @SerialName("unitId")
    val unitId: Long,

    @SerialName("productId")
    val productId: Long?,

    @SerialName("sorder")
    val order: Int,

    @SerialName("base")
    val isBase: Boolean,

    @SerialName("coefficient")
    val coefficient: Double,

    @SerialName("count")
    val count: Double,

    @Serializable(with = BigDecimalSerializer.Nullable::class)
    @SerialName("price")
    val price: BigDecimal?
)