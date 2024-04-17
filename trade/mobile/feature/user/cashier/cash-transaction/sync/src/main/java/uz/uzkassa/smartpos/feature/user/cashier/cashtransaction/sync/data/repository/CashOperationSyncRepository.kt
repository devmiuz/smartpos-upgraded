package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.sync.data.repository

import kotlinx.coroutines.flow.Flow

internal interface CashOperationSyncRepository {

    fun syncCashOperations(): Flow<Unit>
}