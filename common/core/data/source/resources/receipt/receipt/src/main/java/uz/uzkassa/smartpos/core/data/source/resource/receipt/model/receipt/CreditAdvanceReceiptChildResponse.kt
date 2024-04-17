package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPaymentListResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatusResponse
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.DateSerializer
import java.math.BigDecimal
import java.util.*

@Serializable
data class CreditAdvanceReceiptChildResponse(
    @SerialName("uid")
    val uid: String,

    @SerialName("originUid")
    val originUid: String? = null,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("totalPaid")
    val totalPaid: BigDecimal,

    @SerialName("payments")
    val receiptPayments: ReceiptPaymentListResponse,

    @Serializable(with = ReceiptDateSerializer::class)
    @SerialName("receiptDateTime")
    val receiptDate: Date,

    @SerialName("status")
    val status: ReceiptStatusResponse
) {
    private object ReceiptDateSerializer :
        DateSerializer.NotNullable("yyyy-MM-dd'T'HH:mm:ss")
}
