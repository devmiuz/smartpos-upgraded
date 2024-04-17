package uz.uzkassa.smartpos.trade.data.fiscal

import android.content.Context
import uz.uzkassa.smartpos.core.data.source.fiscal.source.receipt.FiscalReceiptSource
import uz.uzkassa.smartpos.core.data.source.fiscal.source.shift.FiscalShiftSource

interface FiscalSourceProvider {

    val fiscalReceiptSource: FiscalReceiptSource

    val fiscalShiftSource: FiscalShiftSource
    
    companion object {
        
        fun instantiate(context: Context): FiscalSourceProvider =
            FiscalSourceProviderImpl(context)
    }
}