package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatusResponse
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class RemoteReceiptResponse(

    @SerialName("uid")
    val uid: String,

    @SerialName("originUid")
    val originUid: String?,

    @SerialName("customerId")
    val customerId: Long? = null,

    @SerialName("loyaltyCardId")
    val loyaltyCardId: Long? = null,

    @SerialName("shiftId")
    val shiftId: Long? = null,

    @SerialName("branchId")
    val branchId: Long,

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
    @SerialName("totalLoyaltyCard")
    val totalLoyaltyCard: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalNds")
    val totalVAT: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalPaid")
    val totalPaid: BigDecimal,

    @SerialName("terminalModel")
    val terminalModel: String?,

    @SerialName("terminalSN")
    val terminalSerialNumber: String?,

    @SerialName("status")
    val status: ReceiptStatusResponse,

    @SerialName("user")
    val userResponse: ReceiptUserResponse,

    @SerialName("receiptDetails")
    val receiptDetails: List<ReceiptDetailResponse>
) {

    val userId: Long
        get() = userResponse.userId

    @Serializable
    data class ReceiptUserResponse(
        @SerialName("id")
        val userId: Long
    )
}