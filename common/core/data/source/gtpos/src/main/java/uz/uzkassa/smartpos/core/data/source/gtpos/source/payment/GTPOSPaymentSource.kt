package uz.uzkassa.smartpos.core.data.source.gtpos.source.payment

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Request.Payment
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Response
import uz.uzkassa.smartpos.core.data.source.gtpos.model.result.GTPOSResult

interface GTPOSPaymentSource {

    fun request(request: Payment): Flow<GTPOSResult<Response.Payment>>
}