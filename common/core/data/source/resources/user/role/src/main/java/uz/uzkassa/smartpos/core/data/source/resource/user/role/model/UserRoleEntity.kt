package uz.uzkassa.smartpos.core.data.source.resource.user.role.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_roles")
data class UserRoleEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_role_code")
    val code: String,

    @ColumnInfo(name = "user_role_type")
    val type: String,

    @ColumnInfo(name = "user_role_priority")
    val priority: Int,

    @ColumnInfo(name = "user_role_name_ru")
    val nameRu: String
)