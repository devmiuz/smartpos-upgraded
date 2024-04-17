package uz.uzkassa.smartpos.feature.launcher.domain.sync

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import uz.uzkassa.smartpos.feature.sync.common.data.repository.SyncRepository
import uz.uzkassa.smartpos.feature.sync.common.model.request.SyncRequest
import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncResult
import javax.inject.Inject

internal class SyncInteractor1 @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val currentBranchPreference: CurrentBranchPreference,
    private val syncRepository: SyncRepository
) {

    fun getSyncState(): Flow<Result<SyncResult>> =
        syncRepository
            .request(SyncRequest(currentBranchPreference.branchId))
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
}