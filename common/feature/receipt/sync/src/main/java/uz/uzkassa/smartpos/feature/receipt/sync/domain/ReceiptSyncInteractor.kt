package uz.uzkassa.smartpos.feature.receipt.sync.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.receipt.sync.data.repository.ReceiptSyncRepository

internal class ReceiptSyncInteractor(
    private val coroutineContextManager: CoroutineContextManager,
    private val receiptSyncRepository: ReceiptSyncRepository
) {

    fun syncReceipts(): Flow<Result<Unit>> {
        return receiptSyncRepository
            .syncReceipts()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}