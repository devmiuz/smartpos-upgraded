package uz.uzkassa.smartpos.core.data.source.resource.shift.report.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.Retrofit
import retrofit2.create

interface ShiftReportRestService {

    fun createShiftReport(jsonElement: JsonElement): Flow<Unit>

    companion object {

        fun instantiate(retrofit: Retrofit): ShiftReportRestService =
            ShiftReportRestServiceImpl(retrofit.create())
    }
}