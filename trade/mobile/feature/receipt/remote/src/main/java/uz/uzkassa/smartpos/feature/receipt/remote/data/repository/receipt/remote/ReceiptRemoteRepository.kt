package uz.uzkassa.smartpos.feature.receipt.remote.data.repository.receipt.remote

import kotlinx.coroutines.flow.Flow

internal interface ReceiptRemoteRepository {

    fun subscribe(): Flow<Unit>
}