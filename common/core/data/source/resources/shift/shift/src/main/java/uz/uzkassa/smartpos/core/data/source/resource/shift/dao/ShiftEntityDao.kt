package uz.uzkassa.smartpos.core.data.source.resource.shift.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.model.ShiftEntity
import java.util.*

@Dao
abstract class ShiftEntityDao : BaseDao.Impl<ShiftEntity>() {

    @Query("SELECT * FROM shifts WHERE shift_status = 'OPEN' AND shift_end_date IS NULL ORDER BY shift_id DESC")
    abstract fun getOpenShiftEntity(): Flow<ShiftEntity?>

    @Query("UPDATE shifts SET shift_status = 'CLOSED', shift_end_date = :endDate WHERE shift_id = (SELECT shift_id FROM shifts WHERE shift_status = 'OPEN' AND shift_end_date IS NULL ORDER BY shift_id DESC)")
    abstract fun closeOpenShift(endDate: Date)
}