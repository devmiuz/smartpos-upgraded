package uz.uzkassa.smartpos.core.data.source.resource.category.model.stats

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.math.BigDecimal

@Entity(tableName = "category_sales_stats")
data class CategorySalesStatsEntity(

    @ColumnInfo(name = "category_sales_stat_discount")
    val discount: BigDecimal,

    @ColumnInfo(name = "category_sales_stat_vat")
    val vat: BigDecimal,

    @ColumnInfo(name = "category_sales_stat_receipt_count")
    val receiptCount: Int,

    @ColumnInfo(name = "category_sales_stat_sales_count")
    val salesCount: Int,

    @ColumnInfo(name = "category_sales_stat_sales_total")
    val salesTotal: Int
)