package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.exception

internal data class CashTransactionSavingException(
    val isOperationNotDefined: Boolean,
    val isOperationAmountNotValid: Boolean
) : Exception() {

    val isPassed: Boolean
        get() = isOperationNotDefined || isOperationAmountNotValid
}