package uz.uzkassa.smartpos.core.data.source.resource.city.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey
    @ColumnInfo(name = "city_id")
    val id: Long,

    @ColumnInfo(name = "city_region_id")
    val regionId: Long,

    @ColumnInfo(name = "city_name_ru")
    val nameRu: String,

    @ColumnInfo(name = "city_name_uz")
    val nameUz: String
)