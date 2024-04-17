package uz.uzkassa.smartpos.core.data.source.resource.terminal.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

internal interface TerminalRestServiceInternal {

    @GET(API_TERMINAL_CHECK)
    fun checkTerminalByBranch(
        @Query(QUERY_BRANCH_ID) branchId: Long,
        @Query(QUERY_TERMINAL_MODEL) terminalModel: String,
        @Query(QUERY_TERMINAL_SERIAL_NUMBER) terminalSerialNumber: String
    ): Flow<Unit>

    private companion object {
        const val API_TERMINAL_CHECK: String = "api/terminal/check"
        const val QUERY_BRANCH_ID: String = "branchId"
        const val QUERY_TERMINAL_MODEL: String = "model"
        const val QUERY_TERMINAL_SERIAL_NUMBER: String = "serialNumber"
    }
}