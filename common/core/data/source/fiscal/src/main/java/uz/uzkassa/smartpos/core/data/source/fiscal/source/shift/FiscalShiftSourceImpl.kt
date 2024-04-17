package uz.uzkassa.smartpos.core.data.source.fiscal.source.shift

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import uz.uzkassa.cto.fiscal.integration.FiscalIntegrationApi
import uz.uzkassa.smartpos.core.data.source.fiscal.mapper.shift.mapToFiscalResult
import uz.uzkassa.smartpos.core.data.source.fiscal.utils.dataOrError
import uz.uzkassa.cto.fiscal.integration.model.shift.finish.FiscalFinishShiftRequest as FinishShiftRequest
import uz.uzkassa.cto.fiscal.integration.model.shift.start.FiscalStartShiftRequest as StartShiftRequest

internal class FiscalShiftSourceImpl(
    private val fiscalIntegrationApi: FiscalIntegrationApi
) : FiscalShiftSource {

    override fun openShift(cashierId: Long, cashierName: String) =
        flow { emit(fiscalIntegrationApi.startShift(StartShiftRequest(cashierId, cashierName))) }
            .map { it.dataOrError()?.mapToFiscalResult() }

    override fun pauseShift() =
        flow { emit(fiscalIntegrationApi.pauseShift()) }
            .map { Unit }

    override fun closeShift() =
        flow { emit(fiscalIntegrationApi.finishShift(FinishShiftRequest())) }
            .map { it.dataOrError()?.mapToFiscalResult() }
}