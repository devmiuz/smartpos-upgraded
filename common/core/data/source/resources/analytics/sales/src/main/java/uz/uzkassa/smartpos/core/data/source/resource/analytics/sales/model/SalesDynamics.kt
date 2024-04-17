package uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model

import java.math.BigDecimal
import java.util.*

data class SalesDynamics(
    val averageReceiptCost: Double,
    val discount: BigDecimal,
    val refund: BigDecimal,
    val salesCard: BigDecimal,
    val salesCash: BigDecimal,
    val salesCount: Double,
    val salesDate: Date,
    val salesTotal: BigDecimal,
    val salesIncome: BigDecimal,
    val vat: BigDecimal
)