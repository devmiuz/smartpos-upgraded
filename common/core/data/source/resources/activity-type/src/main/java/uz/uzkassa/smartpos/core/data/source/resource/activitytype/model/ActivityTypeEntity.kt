package uz.uzkassa.smartpos.core.data.source.resource.activitytype.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_types")
data class ActivityTypeEntity(
    @PrimaryKey
    @ColumnInfo(name = "activity_type_id")
    val id: Long,

    @ColumnInfo(name = "activity_type_parent_id")
    val parentId: Long?,

    @ColumnInfo(name = "activity_type_name")
    val name: String
)