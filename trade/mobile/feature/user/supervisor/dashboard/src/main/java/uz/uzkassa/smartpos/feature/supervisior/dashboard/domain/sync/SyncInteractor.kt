package uz.uzkassa.smartpos.feature.supervisior.dashboard.domain.sync

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies.SupervisorDashboardFeatureArgs
import uz.uzkassa.smartpos.feature.sync.common.data.repository.SyncRepository
import uz.uzkassa.smartpos.feature.sync.common.model.request.SyncRequest
import javax.inject.Inject

class SyncInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val syncRepository: SyncRepository,
    private val supervisorDashboardFeatureArgs: SupervisorDashboardFeatureArgs
) {

    fun sync(): Flow<Result<Unit>> {
        return syncRepository.sync(SyncRequest(supervisorDashboardFeatureArgs.branchId))
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}