package uz.uzkassa.smartpos.core.data.source.resource.region.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "regions")
data class RegionEntity(
    @PrimaryKey
    @ColumnInfo(name = "region_id")
    val id: Long,

    @ColumnInfo(name = "region_name_ru")
    val nameRu: String,

    @ColumnInfo(name = "region_name_uz")
    val nameUz: String
)