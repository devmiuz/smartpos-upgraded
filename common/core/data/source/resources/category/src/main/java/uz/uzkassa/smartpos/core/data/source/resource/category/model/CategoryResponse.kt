package uz.uzkassa.smartpos.core.data.source.resource.category.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.category.model.stats.CategorySalesStatsResponse

@Serializable
data class CategoryResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("parentId")
    val parentId: Long? = null,

    @SerialName("productCount")
    val productCount: Int? = 0,

    @SerialName("enabled")
    val isEnabled: Boolean,

    @SerialName("children")
    val childCategories: List<CategoryResponse>,

    @SerialName("name")
    val name: String,

    @SerialName("salesStats")
    val salesStats: CategorySalesStatsResponse?,

    @SerialName("imageUrl")
    val imageUrl: String? = null
)