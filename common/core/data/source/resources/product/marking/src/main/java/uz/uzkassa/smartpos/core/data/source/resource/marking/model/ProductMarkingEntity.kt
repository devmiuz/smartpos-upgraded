package uz.uzkassa.smartpos.core.data.source.resource.marking.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_marking")
data class ProductMarkingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_marking_id")
    val id: Long,

    @ColumnInfo(name = "product_marking_product_id")
    val productId: Long? = null,

    @ColumnInfo(name = "product_marking")
    val marking: String
)