package uz.uzkassa.smartpos.feature.launcher.domain.sync

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.launcher.data.model.sync.SyncState
import uz.uzkassa.smartpos.feature.launcher.data.repository.sync.SyncRepository
import javax.inject.Inject

internal class SyncInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val syncRepository: SyncRepository
) {

    fun getSyncState(): Flow<Result<SyncState>> =
        syncRepository
            .getSyncState()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)

    fun clearAppDataAndLogout(): Flow<Result<Unit>> =
        syncRepository
            .clearAppDataAndLogout()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
}