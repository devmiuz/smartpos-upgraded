package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation

internal interface CashOperationsRepository {

    fun getAvailableCashOperations(): Flow<List<CashOperation>>
}