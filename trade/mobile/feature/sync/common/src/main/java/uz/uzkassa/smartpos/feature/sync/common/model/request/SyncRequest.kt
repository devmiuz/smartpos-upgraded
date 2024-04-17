package uz.uzkassa.smartpos.feature.sync.common.model.request

import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncResult

data class SyncRequest(
    val branchId: Long?,
    val syncResult: SyncResult? = null
)