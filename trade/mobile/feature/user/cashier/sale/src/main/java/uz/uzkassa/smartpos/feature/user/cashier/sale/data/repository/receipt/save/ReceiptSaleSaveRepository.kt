package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.save

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.save.params.ReceiptSaleSaveParams

internal interface ReceiptSaleSaveRepository {

    fun createReceipt(params: ReceiptSaleSaveParams): Flow<Unit>

    fun printLastReceipt(): Flow<Unit>

    fun clearTempData(isCreditAdvance : Boolean): Flow<Unit>
}