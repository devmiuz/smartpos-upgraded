package uz.uzkassa.smartpos.core.data.source.fiscal.source.shift

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.fiscal.model.shift.FiscalCloseShiftResult
import uz.uzkassa.smartpos.core.data.source.fiscal.model.shift.FiscalOpenShiftResult

interface FiscalShiftSource {

    fun openShift(cashierId: Long, cashierName: String): Flow<FiscalOpenShiftResult?>

    fun pauseShift(): Flow<Unit>

    fun closeShift(): Flow<FiscalCloseShiftResult?>
}