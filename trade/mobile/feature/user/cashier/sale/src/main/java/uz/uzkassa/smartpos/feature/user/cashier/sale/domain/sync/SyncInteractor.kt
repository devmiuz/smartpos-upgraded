package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sync

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.sync.SyncRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs
import javax.inject.Inject

internal class SyncInteractor @Inject constructor(
    private val cashierSaleFeatureArgs: CashierSaleFeatureArgs,
    private val coroutineContextManager: CoroutineContextManager,
    private val syncRepository: SyncRepository
) {
    private var syncResult: SyncResult? = null

    fun sync(): Flow<Result<Unit>> {
        if (syncResult?.isFinished == true) syncResult = null

        return syncRepository.getSyncState(cashierSaleFeatureArgs.branchId)
            .map { Unit }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}