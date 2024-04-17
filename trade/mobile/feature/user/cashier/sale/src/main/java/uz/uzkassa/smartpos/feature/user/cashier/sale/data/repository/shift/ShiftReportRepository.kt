package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.shift

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.fiscal.model.shift.FiscalCloseShiftResult

internal interface ShiftReportRepository {

    fun closeShift(userId: Long): Flow<FiscalCloseShiftResult?>

    fun pauseShift(): Flow<Unit>
}