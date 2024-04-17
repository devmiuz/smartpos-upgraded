package uz.uzkassa.smartpos.core.data.source.resource.product.unit.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "product_units")
data class ProductUnitEntity(
    @PrimaryKey
    @ColumnInfo(name = "product_unit_id")
    val id: Long,

    @ColumnInfo(name = "product_unit_unit_id")
    val unitId: Long,

    @ColumnInfo(name = "product_unit_product_id")
    val productId: Long,

    @ColumnInfo(name = "product_unit_order")
    val order: Int,

    @ColumnInfo(name = "product_unit_is_main")
    val isBase: Boolean,

    @ColumnInfo(name = "product_unit_coefficient")
    val coefficient: Double,

    @ColumnInfo(name = "product_unt_count")
    val count: Double,

    @ColumnInfo(name = "product_unit_price")
    val price: BigDecimal?
)