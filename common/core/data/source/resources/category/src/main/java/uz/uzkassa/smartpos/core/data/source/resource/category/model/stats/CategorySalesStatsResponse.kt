package uz.uzkassa.smartpos.core.data.source.resource.category.model.stats

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class CategorySalesStatsResponse(

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("discount")
    val discount: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("nds")
    val vat: BigDecimal,

    @SerialName("receiptCount")
    val receiptCount: Int,

    @SerialName("salesCount")
    val salesCount: Int,

    @SerialName("salesTotal")
    val salesTotal: Int
)