package uz.uzkassa.smartpos.core.data.source.resource.category.mapper.stats

import uz.uzkassa.smartpos.core.data.source.resource.category.model.stats.CategorySalesStats
import uz.uzkassa.smartpos.core.data.source.resource.category.model.stats.CategorySalesStatsEntity

fun List<CategorySalesStatsEntity>.map() =
    map { it.map() }

fun CategorySalesStatsEntity.map() =
    CategorySalesStats(discount, vat, receiptCount, salesCount, salesTotal)