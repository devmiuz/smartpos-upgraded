package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.AdditionalDetails
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPaymentListResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatusResponse
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.DateSerializer
import java.math.BigDecimal
import java.util.*

@Serializable
data class CreditAdvanceReceiptResponse(
    @SerialName("uid")
    val uid: String,

    @SerialName("originUid")
    val originUid: String? = null,

    @SerialName("customerId")
    val customerId: Long? = null,

    @SerialName("loyaltyCardId")
    val loyaltyCardId: Long? = null,

    @SerialName("shiftId")
    val shiftId: Long? = null,

    @SerialName("branchId")
    val branchId: Long,

    @SerialName("companyId")
    val companyId: Long,

    @Serializable(with = ReceiptDateSerializer::class)
    @SerialName("receiptDateTime")
    val receiptDate: Date,

    @SerialName("lat")
    val receiptLatitude: Double? = null,

    @SerialName("lon")
    val receiptLongitude: Double? = null,

    @SerialName("shiftNo")
    val shiftNumber: Long? = null,

    @SerialName("discountPercent")
    val discountPercent: Double? = null,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalCard")
    val totalCard: BigDecimal? = null,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalCash")
    val totalCash: BigDecimal? = null,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalCost")
    val totalCost: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalDiscount")
    val totalDiscount: BigDecimal? = null,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    val totalExcise: BigDecimal? = null,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalLoyaltyCard")
    val totalLoyaltyCard: BigDecimal? = null,

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

    @SerialName("fiscalSign")
    val fiscalSign: String? = null,

    @SerialName("fiscalUrl")
    val fiscalUrl: String? = null,

    @SerialName("status")
    val status: ReceiptStatusResponse,

    @SerialName("user")
    val userResponse: ReceiptResponse.ReceiptUserResponse,

    @SerialName("customerName")
    val customerName: String?,

    @SerialName("customerPhone")
    val customerContact: String?,

    @SerialName("readOnly")
    val readonly: Boolean?,

    @SerialName("forceToPrint")
    val forceToPrint: Boolean?,

    @SerialName("additional")
    val additionalDetails: AdditionalDetails?,

    @SerialName("receiptDetails")
    val receiptDetails: List<ReceiptDetailResponse>,

    @SerialName("payments")
    val receiptPayments: ReceiptPaymentListResponse,

    @SerialName("billId")
    val paymentBillId: String?,

    @SerialName("child")
    val childResponses: List<CreditAdvanceReceiptChildResponse>
) {

    val userId: Long
        get() = userResponse.userId

    @Serializable
    data class ReceiptUserResponse(
        @SerialName("id")
        val userId: Long
    )

    private object ReceiptDateSerializer :
        DateSerializer.NotNullable("yyyy-MM-dd'T'HH:mm:ss")
}
