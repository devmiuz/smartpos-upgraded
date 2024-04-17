package uz.uzkassa.smartpos.core.data.source.resource.category.mapper

import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.stats.map
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryEntity

fun List<CategoryEntity>.map() =
    map { it.map() }

fun CategoryEntity.map(): Category =
    Category(
        id = id,
        parentId = parentId,
        productCount = productCount,
        isEnabled = isEnabled,
        childCategories = emptyList(),
        name = name,
        salesStats = salesStats?.map(),
        imageUrl = imageUrl
    )