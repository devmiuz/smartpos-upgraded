package uz.uzkassa.smartpos.core.data.source.fiscal.source.receipt

import android.util.Log
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.cto.fiscal.integration.FiscalIntegrationApi
import uz.uzkassa.smartpos.core.data.source.fiscal.mapper.receipt.mapToFiscalRequest
import uz.uzkassa.smartpos.core.data.source.fiscal.mapper.receipt.mapToFiscalResult
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalReceipt
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalReceiptResult
import uz.uzkassa.smartpos.core.data.source.fiscal.utils.dataOrError
import uz.uzkassa.smartpos.core.manager.logger.Logger

internal class FiscalReceiptSourceImpl(
    private val fiscalIntegrationApi: FiscalIntegrationApi
) : FiscalReceiptSource {

    @FlowPreview
    override fun createReceipt(receipt: FiscalReceipt): Flow<FiscalReceiptResult?> {
        Logger.d("Fiscal Receipt", receipt.toString())
        return flowOf(receipt.mapToFiscalRequest())
            .onEach {
                Logger.d("Fiscal Request", it.toString())
            }
            .flatMapConcat {
                flow {
                    emit(fiscalIntegrationApi.createReceipt(it))
                }
            }.map {
                Log.d("TAG", "createReceipt: $it")
                it.dataOrError()?.mapToFiscalResult()
            }
    }
}