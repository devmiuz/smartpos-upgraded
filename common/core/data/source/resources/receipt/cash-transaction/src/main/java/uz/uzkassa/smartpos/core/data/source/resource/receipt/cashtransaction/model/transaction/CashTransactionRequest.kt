package uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.status.CashTransactionStatusRequest
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.DateSerializer
import java.math.BigDecimal
import java.util.*

@Serializable
data class CashTransactionRequest(

    @SerialName("uid")
    val receiptUid: String,

    val userId: Long,

    @SerialName("shiftId")
    val shiftId: Long?,

    @SerialName("branchId")
    val branchId: Long,

    @SerialName("companyId")
    val companyId: Long,

    @Serializable(with = CashTransactionDateSerializer::class)
    @SerialName("receiptDateTime")
    val receiptDate: Date,

    @SerialName("shiftNo")
    val shiftNumber: Long?,

    @SerialName("discountPercent")
    val discountPercent: Double,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalCard")
    val totalCard: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalCash")
    val totalCash: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalCost")
    val totalCost: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalDiscount")
    val totalDiscount: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalNds")
    val totalVAT: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalPaid")
    val totalPaid: BigDecimal,

    val terminalModel: String?,

    val terminalSerialNumber: String?,

    val status: CashTransactionStatusRequest
){
    private object CashTransactionDateSerializer :
        DateSerializer.NotNullable("yyyy-MM-dd'T'HH:mm:ss.SSS")
}