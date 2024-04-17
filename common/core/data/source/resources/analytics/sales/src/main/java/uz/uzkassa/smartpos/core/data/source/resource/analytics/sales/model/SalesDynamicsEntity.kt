package uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.*

@Entity(tableName = "analytics_sales_dynamics")
data class SalesDynamicsEntity(
    @PrimaryKey
    @ColumnInfo(name = "sales_dynamics_date")
    val salesDate: Date,

    @ColumnInfo(name = "sales_dynamics_sales_count")
    val salesCount: Double,

    @ColumnInfo(name = "sales_dynamics_sales_cash")
    val salesCash: BigDecimal,

    @ColumnInfo(name = "sales_dynamics_sales_card")
    val salesCard: BigDecimal,

    @ColumnInfo(name = "sales_dynamics_discount")
    val discount: BigDecimal,

    @ColumnInfo(name = "sales_dynamics_refund")
    val refund: BigDecimal,

    @ColumnInfo(name = "sales_dynamics_vat")
    val vat: BigDecimal,

    @ColumnInfo(name = "sales_dynamics_sales_total")
    val salesTotal: BigDecimal,

    @ColumnInfo(name = "sales_dynamics_average_receipt_cost")
    val averageReceiptCost: Double
)