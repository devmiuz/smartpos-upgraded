package uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination

import androidx.room.ColumnInfo
import androidx.room.Ignore
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductRelation

data class ProductPaginationRelation @Ignore internal constructor(
    @ColumnInfo(name = "isLast")
    val isLast: Boolean,

    @ColumnInfo(name = "isFirst")
    val isFirst: Boolean,

    @Ignore
    val currentPage: Int,

    @ColumnInfo(name = "size")
    val size: Int,

    @Ignore
    val productRelations: List<ProductRelation>
) {

    internal constructor(
        isLast: Boolean,
        isFirst: Boolean,
        size: Int
    ) : this(
        isLast = isLast,
        isFirst = isFirst,
        currentPage = 0,
        size = size,
        productRelations = emptyList()
    )
}