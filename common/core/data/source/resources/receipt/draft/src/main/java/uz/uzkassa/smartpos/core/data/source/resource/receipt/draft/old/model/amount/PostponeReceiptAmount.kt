package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.amount

import java.math.BigDecimal

data class PostponeReceiptAmount(
    val amount: BigDecimal,
    val type: Type
) {

    @Suppress("unused")
    enum class Type {
        CARD, CASH
    }
}