package uz.uzkassa.smartpos.core.data.source.fiscal.model.shift

import java.math.BigDecimal
import java.util.*

data class FiscalCloseShiftResult(
    val startDate: Date?,
    val finishDate: Date?,
    val samSerialNumber: String,
    val shiftNumber: Int,
    val firstReceiptSeq: Long,
    val lastReceiptSeq: Long,
    val totalRefundVAT: BigDecimal,
    val totalRefundCard: BigDecimal,
    val totalRefundCash: BigDecimal,
    val totalRefundCount: Int,
    val totalSaleVAT: BigDecimal,
    val totalSaleCard: BigDecimal,
    val totalSaleCash: BigDecimal,
    val totalSaleCount: Int
)