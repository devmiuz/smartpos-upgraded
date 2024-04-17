package uz.uzkassa.smartpos.core.data.source.resource.unit.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "units")
data class UnitEntity(
    @PrimaryKey
    @ColumnInfo(name = "unit_id")
    val id: Long,

    @ColumnInfo(name = "unit_code")
    val code: Long,

    @ColumnInfo(name = "unit_is_countable")
    val isCountable: Boolean,

    @ColumnInfo(name = "unit_name")
    val name: String,

    @ColumnInfo(name = "unit_name_ru")
    val nameRu: String?,

    @ColumnInfo(name = "unit_name_uz")
    val nameUz: String,

    @ColumnInfo(name = "unit_description")
    val description: String
)