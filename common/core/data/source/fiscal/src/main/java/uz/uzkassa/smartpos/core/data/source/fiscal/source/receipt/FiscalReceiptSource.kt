package uz.uzkassa.smartpos.core.data.source.fiscal.source.receipt

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalReceipt
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.FiscalReceiptResult

interface FiscalReceiptSource {

    fun createReceipt(receipt: FiscalReceipt): Flow<FiscalReceiptResult?>
}