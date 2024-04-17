package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.sync.data.repository

import android.util.Log
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.mapper.mapToRequests
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction.CashTransactionRequest
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.service.CashTransactionRestService
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual

internal class CashOperationSyncRepositoryImpl(
    private val cashTransactionEntityDao: CashTransactionEntityDao,
    private val cashTransactionRestService: CashTransactionRestService
) : CashOperationSyncRepository {

    @FlowPreview
    @UnstableDefault
    override fun syncCashOperations(): Flow<Unit> {
        return flow {
            emit(
                cashTransactionEntityDao.getEntitiesBySyncWithoutOperations(
                    false,
                    listOf(CashOperation.PAID.name, CashOperation.RETURNED.name)
                ).first()
            )
        }
            .map { list -> list.map { it.map() } }
            .flatMapConcat { cashTransactions ->
                if (cashTransactions.isEmpty()) return@flatMapConcat flowOf(Unit)


                val jsonElement: JsonElement = Json.actual.toJson(
                    serializer = ListSerializer(CashTransactionRequest.serializer()),
                    value = cashTransactions.mapToRequests()
                )

                Log.e("Muxtorjon","synCashOperations $jsonElement")

                return@flatMapConcat cashTransactionRestService.createReceiptByBatch(jsonElement)
                    .map { cashTransactions.map { it.receiptUid } }
                    .onEach { cashTransactionEntityDao.updateSyncState(it) }
                    .map { Unit }
                    .flatMapConcat { syncCashOperations() }
            }
    }
}