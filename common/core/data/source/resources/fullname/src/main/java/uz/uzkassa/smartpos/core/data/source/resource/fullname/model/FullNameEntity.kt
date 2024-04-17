package uz.uzkassa.smartpos.core.data.source.resource.fullname.model

import androidx.room.ColumnInfo

data class FullNameEntity(
    @ColumnInfo(name = "full_name_first_name")
    val firstName: String,

    @ColumnInfo(name = "full_name_last_name")
    val lastName: String?,

    @ColumnInfo(name = "full_name_patronymic_name")
    val patronymic: String?
)