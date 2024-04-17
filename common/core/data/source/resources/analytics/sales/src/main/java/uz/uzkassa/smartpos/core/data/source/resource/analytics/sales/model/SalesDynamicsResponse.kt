package uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.DateSerializer
import java.math.BigDecimal
import java.util.*

@Serializable
data class SalesDynamicsResponse(
    @SerialName("averageReceiptCost")
    val averageReceiptCost: Double,

    @Serializable(with = DateSerializer.NotNullable::class)
    @SerialName("dateTime")
    val date: Date,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("discount")
    val discount: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("returned")
    val refund: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("salesCard")
    val salesCard: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("salesCash")
    val salesCash: BigDecimal,

    @SerialName("salesCount")
    val salesCount: Double,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("nds")
    val vat: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("salesTotal")
    val salesTotal: BigDecimal
)