package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.domain

import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.channel.CashOperationBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.exception.CashTransactionSavingException
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.amount.CashAmount
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.amount.CashAmountRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.CashOperationsRepository
import java.math.BigDecimal

internal class CashOperationsInteractor(
    private val cashAmountRepository: CashAmountRepository,
    private val cashOperationBroadcastChannel: CashOperationBroadcastChannel,
    private val cashOperationsRepository: CashOperationsRepository,
    private val coroutineContextManager: CoroutineContextManager
) {
    private var allowedAmount: CashAmount? = null
    private var cashAmount: BigDecimal? = null
    private var operation: CashOperation? = null
    private var message: String? = null

    fun getAllowedAmount(): CashAmount =
        checkNotNull(allowedAmount)

    fun getOperation(): CashOperation =
        checkNotNull(operation)

    fun setOperation(value: CashOperation) {
        operation = value
        cashOperationBroadcastChannel.sendBlocking(checkNotNull(operation))
    }

    fun getTotalCash(): BigDecimal =
        checkNotNull(cashAmount)

    fun setTotalCash(value: String) {
        val totalAmount: BigDecimal = if (value.isNotBlank())
            BigDecimal(removeAllWhiteSpaces(value))
        else BigDecimal.ZERO
        cashAmount = totalAmount
    }

    fun getMessage(): String =
        message ?: ""

    fun setMessage(value: String) {
        message = value
    }

    fun getCashOperations(): Flow<Result<List<CashOperation>>> {
        return cashOperationsRepository.getAvailableCashOperations()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun getAllowedCashAmount(): Flow<Result<CashAmount>> {
        return when (operation) {
            CashOperation.WITHDRAW ->
                cashAmountRepository.getAllowedReturnAddedCash()
            CashOperation.RETURN_FLOW ->
                cashAmountRepository.getAllowedReturnExpenseAmount()
            else ->
                cashAmountRepository.getHavingCashAmount()
        }
            .onEach { allowedAmount = it }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun proceedSavingTransaction(): Flow<Result<Unit>> {
        val isOperationAmountNotValid: Boolean = cashAmount?.let {
            it <= BigDecimal.ZERO ||
                    (operation != CashOperation.INCOME &&
                            it > checkNotNull(allowedAmount).amount)
        } ?: true

        val exception = CashTransactionSavingException(
            isOperationNotDefined = operation == null,
            isOperationAmountNotValid = isOperationAmountNotValid
        )

        return when {
            exception.isPassed -> flowOf(Result.failure(exception))
            else -> flowOf(Result.success(Unit))
        }
    }

    private fun removeAllWhiteSpaces(value: String) =
        value.replace("\\s".toRegex(), "")
}