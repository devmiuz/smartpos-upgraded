package uz.uzkassa.smartpos.feature.receipt.remote.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.receipt.remote.data.repository.receipt.remote.ReceiptRemoteRepository
import javax.inject.Inject

internal class ReceiptRemoteInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val receiptRemoteRepository: ReceiptRemoteRepository
) {

    fun subscribe(): Flow<Unit> =
        receiptRemoteRepository
            .subscribe()
            .flowOn(coroutineContextManager.ioContext)
}