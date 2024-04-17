package uz.uzkassa.smartpos.feature.supervisior.dashboard.data.model

import java.math.BigDecimal
import java.util.*

data class ReceiptTotalDetails(
    val averageReceiptCost: Double,
    val date: Date,
    val discount: BigDecimal,
    val cardTotal: BigDecimal,
    val cashTotal: BigDecimal,
    val count: Double,
    val total: BigDecimal,
    val vat: BigDecimal
)