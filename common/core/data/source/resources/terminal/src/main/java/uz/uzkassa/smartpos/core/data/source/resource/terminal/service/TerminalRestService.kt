package uz.uzkassa.smartpos.core.data.source.resource.terminal.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create

interface TerminalRestService {

    fun checkTerminalByBranch(
        branchId: Long,
        terminalModel: String,
        terminalSerialNumber: String
    ): Flow<Unit>

    companion object {

        fun instantiate(retrofit: Retrofit): TerminalRestService =
            TerminalRestServiceImpl(retrofit.create())
    }
}