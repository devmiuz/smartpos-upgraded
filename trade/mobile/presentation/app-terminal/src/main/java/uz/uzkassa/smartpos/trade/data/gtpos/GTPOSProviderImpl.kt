package uz.uzkassa.smartpos.trade.data.gtpos

import android.content.Context
import uz.uzkassa.smartpos.core.data.source.gtpos.intent.GTPOSLaunchIntent
import uz.uzkassa.smartpos.core.data.source.gtpos.source.batch.GTPOSBatchSource
import uz.uzkassa.smartpos.core.data.source.gtpos.source.payment.GTPOSPaymentSource
import javax.inject.Inject
import uz.uzkassa.smartpos.core.data.source.gtpos.GTPOSProvider as Provider

class GTPOSProviderImpl @Inject constructor(context: Context) : GTPOSProvider {
    private val provider: Provider = Provider.instantiate(context)

    override val launchIntent: GTPOSLaunchIntent by lazy {
        provider.launchIntent
    }

    override val jetpackComponent by lazy { 
        provider.jetpackComponent
    }

    override val batchSource: GTPOSBatchSource by lazy {
        provider.sourceProvider.batchSource
    }
    
    override val paymentSource: GTPOSPaymentSource by lazy {
        provider.sourceProvider.paymentSource
    }
}