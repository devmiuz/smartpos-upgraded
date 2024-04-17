package uz.uzkassa.smartpos.feature.receipt.sync.data.repository

import android.util.Log
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.mapToResponses
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual

internal class ReceiptSyncRepositoryImpl(
    private val receiptEntityDao: ReceiptEntityDao,
    private val receiptRelationDao: ReceiptRelationDao,
    private val receiptRestService: ReceiptRestService
) : ReceiptSyncRepository {

    @FlowPreview
    @UnstableDefault
    override fun syncReceipts(): Flow<Unit> {
        return flow {
            emit(receiptRelationDao.getRelationsBySync(false))
        }
            .flatMapConcat { relations ->
                if (relations.isEmpty()) return@flatMapConcat flowOf(Unit)

                val jsonElement: JsonElement = Json.actual.toJson(
                    serializer = ListSerializer(ReceiptResponse.serializer()),
                    value = relations.mapToResponses()
                )

                Log.e("Muxtorjon", "synReceipts $jsonElement");

                return@flatMapConcat receiptRestService.createReceiptByBatch(jsonElement)
                    .onEach {
                        receiptEntityDao.updateSyncState(relations.map { it.receiptEntity.uid })
                    }
                    .map { }
//                    .flatMapConcat { syncReceipts() }
//                return@flatMapConcat flowOf(Unit)
            }
    }
}