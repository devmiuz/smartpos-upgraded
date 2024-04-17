package uz.uzkassa.smartpos.feature.user.auth.data.repository.shift

import kotlinx.coroutines.flow.Flow

internal interface ShiftReportRepository {

    fun openShiftReport(userId: Long, userName: String): Flow<Unit>
}