package uz.uzkassa.smartpos.core.data.source.gtpos.source

import uz.uzkassa.smartpos.core.data.source.gtpos.source.batch.GTPOSBatchSource
import uz.uzkassa.smartpos.core.data.source.gtpos.source.payment.GTPOSPaymentSource

interface GTPOSSourceProvider {

    val batchSource: GTPOSBatchSource

    val paymentSource: GTPOSPaymentSource
}