package uz.uzkassa.smartpos.core.data.source.resource.category.mapper

import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.stats.map
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryRelation

fun List<CategoryRelation>.map() =
    map { it.map() }

fun CategoryRelation.map(): Category =
    Category(
        id = entity.id,
        parentId = entity.parentId,
        productCount = entity.productCount,
        isEnabled = entity.isEnabled,
        childCategories = relations.map(),
        name = entity.name,
        salesStats = entity.salesStats?.map(),
        imageUrl = entity.imageUrl
    )