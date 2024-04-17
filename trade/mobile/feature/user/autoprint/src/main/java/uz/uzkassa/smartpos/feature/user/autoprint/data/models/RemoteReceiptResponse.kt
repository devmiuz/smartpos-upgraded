package uz.uzkassa.smartpos.feature.user.autoprint.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.DateSerializer
import uz.uzkassa.smartpos.feature.user.autoprint.data.serializer.RemoteReceiptBigDecimalSerializer
import java.math.BigDecimal
import java.util.Date

@Serializable
data class RemoteReceiptResponse(

    @SerialName("uid")
    val receiptUid: String?,

    @SerialName("readOnly")
    val readOnly: Boolean?,
    @SerialName("forceToPrint")
    val forceToPrint: Boolean?,

    @Serializable(with = DateSerializer.Nullable::class)
    @SerialName("receiptDateTime")
    val createdDate: Date?,

    @Serializable(with = RemoteReceiptBigDecimalSerializer.Nullable::class)
    @SerialName("totalCash")
    val totalCash: BigDecimal?,

    @Serializable(with = RemoteReceiptBigDecimalSerializer.Nullable::class)
    @SerialName("totalCard")
    val totalCard: BigDecimal?,

    @Serializable(with = RemoteReceiptBigDecimalSerializer.Nullable::class)
    @SerialName("totalLoyaltyCard")
    val totalLoyaltyCard: BigDecimal?,

    @Serializable(with = RemoteReceiptBigDecimalSerializer.Nullable::class)
    @SerialName("totalCost")
    val totalCost: BigDecimal?,

    @Serializable(with = RemoteReceiptBigDecimalSerializer.Nullable::class)
    @SerialName("totalPaid")
    val totalPaid: BigDecimal?,

    @Serializable(with = RemoteReceiptBigDecimalSerializer.Nullable::class)
    @SerialName("totalNds")
    val totalVAT: BigDecimal?,

    @SerialName("branchId")
    val branchId: Long?,
    @SerialName("terminalSN")
    val terminalSerialNumber: String?,
    @SerialName("terminalModel")
    val terminalModel: String?,
    @SerialName("companyId")
    val companyId: Long?,
    @SerialName("userId")
    val userId: Long?,

    @SerialName("status")
    val status: String?,

    @SerialName("source")
    val source: String?,

    @SerialName("user")
    val user: RemoteReceiptUserResponse?,

    @SerialName("receiptDetails")
    val remoteReceiptDetails: List<RemoteReceiptDetailResponse>?
)
