package uz.uzkassa.smartpos.feature.launcher.data.repository.sync

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.launcher.data.model.sync.SyncState

internal interface SyncRepository {

    fun getSyncState(): Flow<SyncState>

    fun clearAppDataAndLogout(): Flow<Unit>

}