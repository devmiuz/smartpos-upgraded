package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.CreditAdvanceReceiptResponse

@Serializable
data class CreditAdvancePaginationResponse(
    @SerialName("content")
    val creditAdvanceReceipts: List<CreditAdvanceReceiptResponse>,

    @SerialName("last")
    val isLast: Boolean,

    @SerialName("size")
    val size: Int
)