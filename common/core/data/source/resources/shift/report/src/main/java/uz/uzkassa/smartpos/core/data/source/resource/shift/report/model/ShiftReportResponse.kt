package uz.uzkassa.smartpos.core.data.source.resource.shift.report.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.DateSerializer
import java.math.BigDecimal
import java.util.*

@Serializable
data class ShiftReportResponse(

    @Serializable(with = ShiftDateSerializer::class)
    @SerialName("startDateTime")
    val startDate: Date?,

    @Serializable(with = ShiftDateSerializer::class)
    @SerialName("endDateTime")
    val finishDate: Date?,

    @SerialName("fiscalNumber")
    val fiscalShiftNumber: Int,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("ndsReturned")
    val totalRefundVAT: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalCardReturned")
    val totalRefundCard: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalCashReturned")
    val totalRefundCash: BigDecimal,

    @SerialName("receiptsReturned")
    val totalRefundCount: Int,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("nds")
    val totalSaleVAT: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalCard")
    val totalSaleCard: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalCash")
    val totalSaleCash: BigDecimal,

    @SerialName("receipts")
    val totalSaleCount: Int,

    @SerialName("userId")
    val userId: Long
) {

    private object ShiftDateSerializer :
        DateSerializer.Nullable("yyyy-MM-dd'T'HH:mm:ss.SSS")
}