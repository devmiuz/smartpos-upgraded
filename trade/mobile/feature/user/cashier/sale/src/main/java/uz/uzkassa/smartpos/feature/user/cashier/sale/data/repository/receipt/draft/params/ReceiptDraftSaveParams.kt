package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.params

import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.save.params.ReceiptSaleSaveParams

internal data class ReceiptDraftSaveParams(
    val receiptSaveParams: ReceiptSaleSaveParams,
    val name: String
)