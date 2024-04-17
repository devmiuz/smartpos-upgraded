package uz.uzkassa.smartpos.core.data.source.resource.shift.report.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement

internal class ShiftReportRestServiceImpl(
    private val shiftReportRestServiceInternal: ShiftReportRestServiceInternal
) : ShiftReportRestService {

    override fun createShiftReport(jsonElement: JsonElement): Flow<Unit> {
        return shiftReportRestServiceInternal.createShiftReport(jsonElement)
    }
}