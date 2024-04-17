package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import java.util.*

data class ReceiptDraft(
    val id: Long,
    val name: String,
    val isRemote: Boolean,
    val receipt: Receipt
)