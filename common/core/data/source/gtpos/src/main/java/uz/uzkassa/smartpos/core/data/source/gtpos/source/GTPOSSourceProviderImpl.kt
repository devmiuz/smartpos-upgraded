package uz.uzkassa.smartpos.core.data.source.gtpos.source

import uz.uzkassa.smartpos.core.data.source.gtpos.observer.GTConnectObserver
import uz.uzkassa.smartpos.core.data.source.gtpos.source.batch.GTPOSBatchSource
import uz.uzkassa.smartpos.core.data.source.gtpos.source.batch.GTPOSBatchSourceImpl
import uz.uzkassa.smartpos.core.data.source.gtpos.source.payment.GTPOSPaymentSource
import uz.uzkassa.smartpos.core.data.source.gtpos.source.payment.GTPOSPaymentSourceImpl

internal class GTPOSSourceProviderImpl(
    private val observer: GTConnectObserver
) : GTPOSSourceProvider {

    override val batchSource: GTPOSBatchSource by lazy {
        GTPOSBatchSourceImpl(observer)
    }

    override val paymentSource: GTPOSPaymentSource by lazy {
        GTPOSPaymentSourceImpl(observer)
    }
}