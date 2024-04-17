package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model

import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.amount.PostponeReceiptAmount
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.detail.PostponeReceiptDetail
import java.math.BigDecimal
import java.util.*

@Deprecated("")
data class PostponeReceipt(
    val id: Long,
    val uid: String?,
    val receiptDate: Date?,
    val cardNumber: String?,
    val amount: BigDecimal,
    val discountAmount: BigDecimal?,
    val discountPercent: Double?,
    val name: String,
    val receiptAmounts: List<PostponeReceiptAmount>,
    val receiptDetails: List<PostponeReceiptDetail>
) {

    val isExternal: Boolean
        get() = uid != null && receiptDate != null
}