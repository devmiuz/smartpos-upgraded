package uz.uzkassa.smartpos.core.data.source.resource.shift.report.model

import java.math.BigDecimal
import java.util.*

data class ShiftReport(
    val startDate: Date?,
    val finishDate: Date?,
    val fiscalShiftNumber: Int,
    val totalRefundVAT: BigDecimal,
    val totalRefundCard: BigDecimal,
    val totalRefundCash: BigDecimal,
    val totalRefundCount: Int,
    val totalSaleVAT: BigDecimal,
    val totalSaleCard: BigDecimal,
    val totalSaleCash: BigDecimal,
    val totalSaleCount: Int
)