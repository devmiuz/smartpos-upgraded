package uz.uzkassa.smartpos.core.data.source.gtpos.source.batch

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Response.Batch
import uz.uzkassa.smartpos.core.data.source.gtpos.model.result.GTPOSResult

interface GTPOSBatchSource {

    fun batchClose(): Flow<GTPOSResult<Batch>>

    fun batchReport(): Flow<GTPOSResult<Batch>>
}