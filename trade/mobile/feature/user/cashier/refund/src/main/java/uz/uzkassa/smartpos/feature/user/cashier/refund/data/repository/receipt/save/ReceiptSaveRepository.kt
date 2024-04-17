package uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.save

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.save.params.ReceiptSaveParams

internal interface ReceiptSaveRepository {

    fun createReceipt(params: ReceiptSaveParams): Flow<Unit>

    fun printLastReceipt(): Flow<Unit>

    fun clearTempData(): Flow<Unit>
}