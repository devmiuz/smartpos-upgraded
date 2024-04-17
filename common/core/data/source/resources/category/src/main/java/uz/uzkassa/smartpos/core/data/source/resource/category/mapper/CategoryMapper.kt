package uz.uzkassa.smartpos.core.data.source.resource.category.mapper

import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.stats.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryResponse

fun List<Category>.mapToResponses() =
    map { it.mapToResponse() }

fun Category.mapToResponse(): CategoryResponse =
    CategoryResponse(
        id = id,
        parentId = parentId,
        productCount = productCount,
        isEnabled = isEnabled,
        childCategories = childCategories.mapToResponses(),
        name = name,
        salesStats = salesStats?.mapToResponse(),
        imageUrl = imageUrl
    )