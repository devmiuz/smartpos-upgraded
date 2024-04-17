package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.sync.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.sync.data.repository.CashOperationSyncRepository

internal class CashOperationSyncInteractor(
    private val coroutineContextManager: CoroutineContextManager,
    private val cashOperationSyncRepository: CashOperationSyncRepository
) {

    fun syncCashOperations(): Flow<Result<Unit>> {
        return cashOperationSyncRepository
            .syncCashOperations()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}