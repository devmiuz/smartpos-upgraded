package uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "branches")
data class BranchEntity(
    @PrimaryKey
    @ColumnInfo(name = "branch_id")
    val id: Long,

    @ColumnInfo(name = "branch_is_deleted")
    val isDeleted: Boolean,

    @ColumnInfo(name = "branch_is_fiscal")
    val isFiscal: Boolean,

    @ColumnInfo(name = "branch_activity_type_id")
    val activityTypeId: Long?,

    @ColumnInfo(name = "branch_region_id")
    val regionId: Long?,

    @ColumnInfo(name = "branch_city_id")
    val cityId: Long?,

    @ColumnInfo(name = "branch_name")
    val name: String,

    @ColumnInfo(name = "branch_description")
    val description: String?,

    @ColumnInfo(name = "branch_address")
    val address: String?
)