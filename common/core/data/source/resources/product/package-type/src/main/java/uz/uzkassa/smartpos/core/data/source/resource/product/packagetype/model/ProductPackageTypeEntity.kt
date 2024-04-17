package uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_package_types")
data class ProductPackageTypeEntity(
    @PrimaryKey
    @ColumnInfo(name = "product_package_type_id")
    val id: Long,

    @ColumnInfo(name = "product_package_type_code")
    val code: Long,

    @ColumnInfo(name = "product_package_type_checksum")
    val checksum: Int,

    @ColumnInfo(name = "product_package_type_is_countable")
    val isCountable: Boolean,

    @ColumnInfo(name = "product_package_type_name")
    val name: String,

    @ColumnInfo(name = "product_package_type_name_ru")
    val nameRu: String,

    @ColumnInfo(name = "product_package_type_name_uz")
    val nameUz: String,

    @ColumnInfo(name = "product_package_type_description")
    val description: String
)