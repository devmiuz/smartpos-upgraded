package uz.uzkassa.smartpos.trade.data.fiscal

import android.content.Context
import uz.uzkassa.smartpos.core.data.source.fiscal.FiscalSourceProvider as Provider

class FiscalSourceProviderImpl(context: Context) : FiscalSourceProvider {
    private val provider = Provider.instantiate(context)
    
    override val fiscalReceiptSource by lazy { 
        provider.fiscalReceiptSource
    }
    
    override val fiscalShiftSource by lazy { 
        provider.fiscalShiftSource
    }
}