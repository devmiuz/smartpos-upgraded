package uz.uzkassa.smartpos.core.data.source.resource.shift.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "shifts")
data class ShiftEntity(
    @PrimaryKey
    @ColumnInfo(name = "shift_id")
    val id: Long,

    @ColumnInfo(name = "shift_user_id")
    val userId: Long,

    @ColumnInfo(name = "shift_start_date")
    val startDate: Date,

    @ColumnInfo(name = "shift_end_date")
    val endDate: Date?,

    @ColumnInfo(name = "shift_fiscal_number")
    val shift_fiscal_number: Long?,

    @ColumnInfo(name = "shift_number")
    val number: Long,

    @ColumnInfo(name = "shift_status")
    val status: String
)