package uz.uzkassa.smartpos.core.data.source.gtpos.source.batch

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Request
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Response.Batch
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Type
import uz.uzkassa.smartpos.core.data.source.gtpos.model.result.GTPOSResult
import uz.uzkassa.smartpos.core.data.source.gtpos.observer.GTConnectObserver

internal class GTPOSBatchSourceImpl(
    private val observer: GTConnectObserver
) : GTPOSBatchSource {

    override fun batchClose(): Flow<GTPOSResult<Batch>> {
        return observer.request(Request.Batch(Type.BATCH_CLOSE))
    }

    override fun batchReport(): Flow<GTPOSResult<Batch>> {
        return observer.request(Request.Batch(Type.BATCH_REPORT))
    }
}