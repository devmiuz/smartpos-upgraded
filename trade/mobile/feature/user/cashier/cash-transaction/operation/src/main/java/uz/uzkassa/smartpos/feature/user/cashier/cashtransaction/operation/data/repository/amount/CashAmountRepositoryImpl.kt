package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.amount

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.amount.CashAmount
import java.math.BigDecimal
import javax.inject.Inject

internal class CashAmountRepositoryImpl @Inject constructor(
    private val cashTransactionEntityDao: CashTransactionEntityDao
) : CashAmountRepository {

    override fun getHavingCashAmount(): Flow<CashAmount> {
        return flow { emit(cashTransactionEntityDao.getCashTotalAmount()) }
            .map {
                val totalAmount: BigDecimal = it ?: BigDecimal.ZERO
                CashAmount(totalAmount)
            }
    }

    @FlowPreview
    override fun getAllowedReturnAddedCash(): Flow<CashAmount> {
        return flow { emit(cashTransactionEntityDao.getCashTotalAmount()) }
            .map { it ?: BigDecimal.ZERO }
            .flatMapConcat { havingCashAmount ->
                flow {
                    val totalAddedCash: BigDecimal =
                        cashTransactionEntityDao
                            .getCashTotalAmountByCashOperation(CashOperation.INCOME.name)
                            ?: BigDecimal.ZERO

                    val totalReturnedAddedCash: BigDecimal =
                        cashTransactionEntityDao
                            .getCashTotalAmountByCashOperation(CashOperation.WITHDRAW.name)
                            ?: BigDecimal.ZERO

                    val totalAmount: BigDecimal =
                        totalAddedCash - totalReturnedAddedCash

                    val cashAmount: CashAmount =
                        if (totalAmount <= havingCashAmount) CashAmount(totalAmount)
                        else CashAmount(havingCashAmount)

                    emit(cashAmount)
                }
            }
    }

    override fun getAllowedReturnExpenseAmount(): Flow<CashAmount> {
        return flow {
            val totalExpense: BigDecimal = cashTransactionEntityDao
                .getCashTotalAmountByCashOperation(CashOperation.FLOW.name)
                ?: BigDecimal.ZERO

            val totalReturnedExpense: BigDecimal = cashTransactionEntityDao
                .getCashTotalAmountByCashOperation(CashOperation.RETURN_FLOW.name)
                ?: BigDecimal.ZERO
            emit(CashAmount(totalExpense - totalReturnedExpense))
        }
    }
}