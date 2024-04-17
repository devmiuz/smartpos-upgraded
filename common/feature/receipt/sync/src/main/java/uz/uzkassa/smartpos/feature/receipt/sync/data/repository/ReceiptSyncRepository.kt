package uz.uzkassa.smartpos.feature.receipt.sync.data.repository

import kotlinx.coroutines.flow.Flow

internal interface ReceiptSyncRepository {

    fun syncReceipts(): Flow<Unit>
}