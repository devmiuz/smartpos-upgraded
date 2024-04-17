package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.save

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.save.params.CashTransactionSaveParams

internal interface CashTransactionSaveRepository {

    fun createCashTransaction(params: CashTransactionSaveParams): Flow<Unit>

    fun printLastCashTransaction(): Flow<Unit>

    fun clearTempData(): Flow<Unit>
}