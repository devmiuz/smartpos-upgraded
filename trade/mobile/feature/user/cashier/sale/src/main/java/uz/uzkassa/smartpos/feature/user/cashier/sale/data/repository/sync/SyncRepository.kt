package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.sync

import kotlinx.coroutines.flow.Flow

internal interface SyncRepository {

    fun getSyncState(branchId: Long): Flow<Unit>
}