package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.detail

import java.math.BigDecimal

data class PostponeReceiptDetail(
    val productId: Long?,
    val unitId: Long?,
    val amount: BigDecimal,
    val quantity: Double
)