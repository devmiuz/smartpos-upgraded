package uz.uzkassa.smartpos.core.data.source.resource.category.mapper.stats

import uz.uzkassa.smartpos.core.data.source.resource.category.model.stats.CategorySalesStats
import uz.uzkassa.smartpos.core.data.source.resource.category.model.stats.CategorySalesStatsEntity
import uz.uzkassa.smartpos.core.data.source.resource.category.model.stats.CategorySalesStatsResponse

fun List<CategorySalesStatsResponse>.map() =
    map { it.map() }

fun List<CategorySalesStatsResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun CategorySalesStatsResponse.map(): CategorySalesStats =
    CategorySalesStats(
        discount = discount,
        vat = vat,
        receiptCount = receiptCount,
        salesCount = salesCount,
        salesTotal = salesTotal
    )

fun CategorySalesStatsResponse.mapToEntity() =
    CategorySalesStatsEntity(
        discount = discount,
        vat = vat,
        receiptCount = receiptCount,
        salesCount = salesCount,
        salesTotal = salesTotal
    )