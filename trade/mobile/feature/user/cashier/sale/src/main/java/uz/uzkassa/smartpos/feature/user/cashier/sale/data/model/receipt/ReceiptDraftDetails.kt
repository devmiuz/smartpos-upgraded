package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.receipt

import java.math.BigDecimal

internal data class ReceiptDraftDetails(
    val receiptDetailsCount: Int,
    val totalAmount: BigDecimal
)