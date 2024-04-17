package uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt

import java.util.*

data class FiscalReceiptResult(
    val receiptSeq: Int? = null,
    val shiftNumber: Int? = null,
    val createdDate: Date? = null,

    val receiptNumberInCurrentShift: Int? = null,
    val samSerialNumber: String? = null,
    val fiscalSign: String? = null,
    val fiscalUrl: String? = null
)