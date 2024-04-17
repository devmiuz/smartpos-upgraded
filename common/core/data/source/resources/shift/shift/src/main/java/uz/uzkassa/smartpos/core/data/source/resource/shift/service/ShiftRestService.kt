package uz.uzkassa.smartpos.core.data.source.resource.shift.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.shift.model.ShiftResponse

interface ShiftRestService {

    fun openShift(fiscalShiftNumber: Int?): Flow<ShiftResponse>

    fun pauseShift(): Flow<Unit>

    fun closeShift(fiscalShiftNumber: Int?): Flow<Unit>

    companion object {

        fun instantiate(retrofit: Retrofit): ShiftRestService =
            ShiftRestServiceImpl(retrofit.create())
    }
}