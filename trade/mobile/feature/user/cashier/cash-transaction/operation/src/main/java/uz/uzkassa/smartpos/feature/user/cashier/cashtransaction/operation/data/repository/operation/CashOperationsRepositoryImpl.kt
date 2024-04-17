package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import javax.inject.Inject

internal class CashOperationsRepositoryImpl @Inject constructor(

) : CashOperationsRepository {

    override fun getAvailableCashOperations(): Flow<List<CashOperation>> {
        val operations = arrayListOf(
            CashOperation.INCASSATION,
            CashOperation.INCOME,
            CashOperation.WITHDRAW,
            CashOperation.FLOW,
            CashOperation.RETURN_FLOW
        )
        return flowOf(operations)
    }
}