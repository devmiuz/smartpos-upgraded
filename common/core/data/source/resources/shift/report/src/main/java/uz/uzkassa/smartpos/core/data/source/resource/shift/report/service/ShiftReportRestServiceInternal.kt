package uz.uzkassa.smartpos.core.data.source.resource.shift.report.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.POST

internal interface ShiftReportRestServiceInternal {

    @POST(API_SHIFTS_REPORT)
    fun createShiftReport(@Body jsonElement: JsonElement): Flow<Unit>

    private companion object {
        const val API_SHIFTS_REPORT: String = "/api/shifts/report"
    }
}