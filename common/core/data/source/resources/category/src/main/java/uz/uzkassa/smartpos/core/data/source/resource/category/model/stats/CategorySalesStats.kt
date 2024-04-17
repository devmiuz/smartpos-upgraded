package uz.uzkassa.smartpos.core.data.source.resource.category.model.stats

import java.math.BigDecimal

data class CategorySalesStats(
    val discount: BigDecimal,
    val vat: BigDecimal,
    val receiptCount: Int,
    val salesCount: Int,
    val salesTotal: Int
)