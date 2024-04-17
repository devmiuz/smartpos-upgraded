package uz.uzkassa.smartpos.core.data.source.fiscal

import android.content.Context
import uz.uzkassa.cto.fiscal.integration.FiscalIntegrationApi
import uz.uzkassa.smartpos.core.data.source.fiscal.source.receipt.FiscalReceiptSource
import uz.uzkassa.smartpos.core.data.source.fiscal.source.receipt.FiscalReceiptSourceImpl
import uz.uzkassa.smartpos.core.data.source.fiscal.source.shift.FiscalShiftSource
import uz.uzkassa.smartpos.core.data.source.fiscal.source.shift.FiscalShiftSourceImpl

internal class FiscalSourceProviderImpl(context: Context) : FiscalSourceProvider {
    private val fiscalIntegrationApi by lazy { FiscalIntegrationApi(context) }

    override val fiscalReceiptSource: FiscalReceiptSource by lazy {
        FiscalReceiptSourceImpl(fiscalIntegrationApi)
    }

    override val fiscalShiftSource: FiscalShiftSource by lazy {
        FiscalShiftSourceImpl(fiscalIntegrationApi)
    }
}