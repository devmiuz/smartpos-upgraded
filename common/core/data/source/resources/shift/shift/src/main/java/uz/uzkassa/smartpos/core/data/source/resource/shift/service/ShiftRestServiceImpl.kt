package uz.uzkassa.smartpos.core.data.source.resource.shift.service

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.shift.model.ShiftResponse

internal class ShiftRestServiceImpl(
    private val restServiceInternal: ShiftRestServiceInternal
) : ShiftRestService {

    override fun openShift(fiscalShiftNumber: Int?): Flow<ShiftResponse> {
        return restServiceInternal.let {
            return@let if (fiscalShiftNumber == null) it.openShift()
            else it.openShift(fiscalShiftNumber)
        }
    }

    override fun pauseShift(): Flow<Unit> {
        return restServiceInternal.pauseShift()
    }

    override fun closeShift(fiscalShiftNumber: Int?): Flow<Unit> {
        return restServiceInternal.let {
            return@let if (fiscalShiftNumber == null) it.closeShift()
            else it.closeShift(fiscalShiftNumber)
        }
    }
}