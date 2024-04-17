package uz.uzkassa.smartpos.core.data.source.resource.category.mapper.stats

import uz.uzkassa.smartpos.core.data.source.resource.category.model.stats.CategorySalesStats
import uz.uzkassa.smartpos.core.data.source.resource.category.model.stats.CategorySalesStatsResponse

fun List<CategorySalesStats>.mapToResponses() =
    map { it.mapToResponse() }

fun CategorySalesStats.mapToResponse(): CategorySalesStatsResponse =
    CategorySalesStatsResponse(
        discount = discount,
        vat = vat,
        receiptCount = receiptCount,
        salesCount = salesCount,
        salesTotal = salesTotal
    )