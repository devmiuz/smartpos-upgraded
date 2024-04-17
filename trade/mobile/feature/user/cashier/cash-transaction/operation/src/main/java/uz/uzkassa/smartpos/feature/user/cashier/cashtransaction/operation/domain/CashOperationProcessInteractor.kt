package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.domain

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.amount.CashAmount
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.process.CashOperationsProcessDetails
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.save.CashTransactionSaveRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.save.params.CashTransactionSaveParams
import java.math.BigDecimal
import javax.inject.Inject

internal class CashOperationProcessInteractor @Inject constructor(
    private val cashTransactionSaveRepository: CashTransactionSaveRepository,
    private val cashOperationsInteractor: CashOperationsInteractor,
    private val coroutineContextManager: CoroutineContextManager
) {
    private var params: CashTransactionSaveParams? = null

    fun getCashOperationsProcessDetails(): CashOperationsProcessDetails =
        CashOperationsProcessDetails(
            allowedAmount = cashOperationsInteractor.getAllowedAmount().amount,
            cashAmount = cashOperationsInteractor.getTotalCash(),
            cashOperation = cashOperationsInteractor.getOperation()
        )

    @FlowPreview
    fun createCashTransaction(): Flow<Result<Unit>> {
        return getActualParams()
            .flatMapConcat { params ->
                cashTransactionSaveRepository
                    .createCashTransaction(params)
                    .flatMapResult()
                    .flowOn(coroutineContextManager.ioContext)
            }
    }

    fun printLastCashTransaction(): Flow<Result<Unit>> {
        return cashTransactionSaveRepository
            .printLastCashTransaction()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun clearCashTransactionData(): Flow<Unit> {
        return cashTransactionSaveRepository.clearTempData()
            .flowOn(coroutineContextManager.ioContext)
    }

    private fun getActualParams(): Flow<CashTransactionSaveParams> {
        return flow {
            if (params == null) {
                val cashAmount: CashAmount = cashOperationsInteractor.getAllowedAmount()
                val totalCash: BigDecimal = cashOperationsInteractor.getTotalCash()
                val cashOperation: CashOperation = cashOperationsInteractor.getOperation()
                val message: String = cashOperationsInteractor.getMessage()

                params = CashTransactionSaveParams(
                    allowedAmount = cashOperationsInteractor.getAllowedAmount().amount,
                    cashAmount = cashAmount,
                    cashOperation = cashOperation,
                    totalCash = totalCash,
                    message = message
                )
                emit(checkNotNull(params))
            }
        }
    }
}