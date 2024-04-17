package uz.uzkassa.smartpos.core.data.source.resource.category.mapper

import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.stats.map
import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.stats.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryEntity
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryResponse

fun List<CategoryResponse>.map() =
    map { it.map() }

fun List<CategoryResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun CategoryResponse.map(): Category =
    Category(
        id = id,
        parentId = parentId,
        productCount = productCount ?: 0,
        isEnabled = isEnabled,
        childCategories = childCategories.map(),
        name = name,
        salesStats = salesStats?.map(),
        imageUrl = imageUrl
    )

fun CategoryResponse.mapToEntity() =
    CategoryEntity(
        id = id,
        parentId = parentId,
        productCount = productCount ?: 0,
        isEnabled = isEnabled,
        childCategoryIds = childCategories.map { it.id }.toLongArray(),
        name = name,
        salesStats = salesStats?.mapToEntity(),
        imageUrl = imageUrl
    )