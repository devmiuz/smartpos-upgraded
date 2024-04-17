package uz.uzkassa.smartpos.trade.data.gtpos

import android.content.Context
import uz.uzkassa.smartpos.core.data.source.gtpos.intent.GTPOSLaunchIntent
import uz.uzkassa.smartpos.core.data.source.gtpos.jetpack.GTPOSJetpackComponent
import uz.uzkassa.smartpos.core.data.source.gtpos.source.batch.GTPOSBatchSource
import uz.uzkassa.smartpos.core.data.source.gtpos.source.payment.GTPOSPaymentSource

interface GTPOSProvider {

    val launchIntent: GTPOSLaunchIntent

    val jetpackComponent: GTPOSJetpackComponent

    val batchSource: GTPOSBatchSource

    val paymentSource: GTPOSPaymentSource

    companion object {

        fun instantiate(context: Context): GTPOSProvider =
            GTPOSProviderImpl(context)
    }
}