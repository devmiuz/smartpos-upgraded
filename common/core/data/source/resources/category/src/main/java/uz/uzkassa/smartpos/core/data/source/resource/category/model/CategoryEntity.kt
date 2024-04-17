package uz.uzkassa.smartpos.core.data.source.resource.category.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import uz.uzkassa.smartpos.core.data.source.resource.category.model.stats.CategorySalesStatsEntity

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "category_id")
    val id: Long,

    @ColumnInfo(name = "category_parent_id")
    val parentId: Long?,

    @ColumnInfo(name = "category_product_count")
    val productCount: Int,

    @ColumnInfo(name = "category_enabled")
    val isEnabled: Boolean,

    @ColumnInfo(name = "children")
    val childCategoryIds: LongArray,

    @ColumnInfo(name = "category_name")
    val name: String,

    @ColumnInfo(name = "category_image_url")
    val imageUrl: String?,

    @Embedded
    val salesStats: CategorySalesStatsEntity?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CategoryEntity

        if (id != other.id) return false
        if (parentId != other.parentId) return false
        if (productCount != other.productCount) return false
        if (isEnabled != other.isEnabled) return false
        if (imageUrl != other.imageUrl) return false
        if (!childCategoryIds.contentEquals(other.childCategoryIds)) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (parentId?.hashCode() ?: 0)
        result = 31 * result + productCount
        result = 31 * result + isEnabled.hashCode()
        result = 31 * result + childCategoryIds.contentHashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + imageUrl.hashCode()
        return result
    }
}