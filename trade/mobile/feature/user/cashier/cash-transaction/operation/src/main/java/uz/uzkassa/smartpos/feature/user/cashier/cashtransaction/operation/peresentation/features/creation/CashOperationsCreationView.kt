package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.creation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.amount.CashAmount
import java.math.BigDecimal

internal interface CashOperationsCreationView : MvpView {

    fun onSuccessCashOperations(encashmentOperations: List<CashOperation>)

    fun onCashOperationChanged(cashOperation: CashOperation)

    fun onLoadingCashAmount()

    fun onSuccessCashAmount(cashAmount: CashAmount, cashOperation: CashOperation)

    fun onFailureCashAmount(throwable: Throwable)

    fun onCashAmountChanged(amount: BigDecimal)

    fun onLoadingCashOperationValidation()

    fun onSuccessCashOperationValidation()

    fun onFailureCashOperationValidation(throwable: Throwable)

    fun onShowCashOperationProcessView()

    fun onFailureCashOperationSavingOperationNotDefined()

    fun onFailureCashOperationSavingAmountNotValid()
}