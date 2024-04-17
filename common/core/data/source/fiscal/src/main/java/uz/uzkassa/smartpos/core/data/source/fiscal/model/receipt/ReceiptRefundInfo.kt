package uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt

data class ReceiptRefundInfo(
    val terminalID: String?,
    val receiptSeq: String?,
    val fiscalReceiptCreatedDate: String?,
    val fiscalSign: String?
)