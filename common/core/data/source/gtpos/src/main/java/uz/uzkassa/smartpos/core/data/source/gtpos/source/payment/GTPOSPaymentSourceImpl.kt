package uz.uzkassa.smartpos.core.data.source.gtpos.source.payment

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Request
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Response.Payment
import uz.uzkassa.smartpos.core.data.source.gtpos.model.result.GTPOSResult
import uz.uzkassa.smartpos.core.data.source.gtpos.observer.GTConnectObserver

internal class GTPOSPaymentSourceImpl(
    private val observer: GTConnectObserver
) : GTPOSPaymentSource {

    override fun request(request: Request.Payment): Flow<GTPOSResult<Payment>> {
        return observer.request(request)
    }
}