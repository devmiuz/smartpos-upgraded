package uz.uzkassa.smartpos.core.data.source.resource.shift.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import uz.uzkassa.smartpos.core.data.source.resource.shift.model.ShiftResponse

internal interface ShiftRestServiceInternal {

    @POST(API_SHIFTS)
    fun openShift(): Flow<ShiftResponse>

    @POST(API_SHIFTS)
    fun openShift(
        @Query(QUERY_FISCAL_SHIFT_NUMBER) fiscalShiftNumber: Int
    ): Flow<ShiftResponse>

    @PUT(API_SHIFTS_SESSION)
    fun pauseShift(): Flow<Unit>

    @PUT(API_SHIFTS)
    fun closeShift(): Flow<Unit>

    @PUT(API_SHIFTS)
    fun closeShift(
        @Query(QUERY_FISCAL_SHIFT_NUMBER) fiscalShiftNumber: Int
    ): Flow<Unit>

    private companion object {
        const val API_SHIFTS: String = "api/shifts"
        const val API_SHIFTS_SESSION: String = "api/shifts/session"
        const val QUERY_FISCAL_SHIFT_NUMBER: String = "fiscalNumber"
    }
}