package uz.uzkassa.smartpos.core.data.source.resource.customer.model.type

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_types")
data class CustomerTypeEntity(
    @PrimaryKey
    @ColumnInfo(name = "customer_type")
    val type: String,

    @ColumnInfo(name = "customer_type_name_ru")
    val nameRu: String
)