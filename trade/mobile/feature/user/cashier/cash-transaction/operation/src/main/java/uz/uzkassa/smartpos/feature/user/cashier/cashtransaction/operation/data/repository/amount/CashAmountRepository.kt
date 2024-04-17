package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.amount

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.amount.CashAmount

internal interface CashAmountRepository {

    fun getHavingCashAmount(): Flow<CashAmount>

    fun getAllowedReturnExpenseAmount(): Flow<CashAmount>

    fun getAllowedReturnAddedCash(): Flow<CashAmount>
}