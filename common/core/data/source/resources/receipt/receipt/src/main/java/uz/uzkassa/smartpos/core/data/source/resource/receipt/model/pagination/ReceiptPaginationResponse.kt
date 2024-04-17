package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse

@Serializable
data class ReceiptPaginationResponse(
    @SerialName("content")
    val receipts: List<ReceiptResponse>,

    @SerialName("last")
    val isLast: Boolean,

    @SerialName("size")
    val size: Int
)