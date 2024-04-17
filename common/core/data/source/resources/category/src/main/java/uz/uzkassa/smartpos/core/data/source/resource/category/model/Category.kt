package uz.uzkassa.smartpos.core.data.source.resource.category.model

import uz.uzkassa.smartpos.core.data.source.resource.category.model.stats.CategorySalesStats

data class Category(
    val id: Long,
    val parentId: Long?,
    val productCount: Int,
    val isEnabled: Boolean,
    val childCategories: List<Category>,
    val name: String,
    val salesStats: CategorySalesStats?,
    val imageUrl: String?
)