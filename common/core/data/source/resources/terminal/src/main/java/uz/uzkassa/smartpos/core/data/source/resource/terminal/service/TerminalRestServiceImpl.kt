package uz.uzkassa.smartpos.core.data.source.resource.terminal.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import uz.uzkassa.smartpos.core.data.source.resource.BuildConfig

internal class TerminalRestServiceImpl(
    private val restServiceInternal: TerminalRestServiceInternal
) : TerminalRestService {

    override fun checkTerminalByBranch(
        branchId: Long,
        terminalModel: String,
        terminalSerialNumber: String
    ): Flow<Unit> {
        return restServiceInternal
            .checkTerminalByBranch(branchId, terminalModel, terminalSerialNumber)
            .catch {
//                if (BuildConfig.DEBUG) emit(true) else
                throw it
            }
    }
}