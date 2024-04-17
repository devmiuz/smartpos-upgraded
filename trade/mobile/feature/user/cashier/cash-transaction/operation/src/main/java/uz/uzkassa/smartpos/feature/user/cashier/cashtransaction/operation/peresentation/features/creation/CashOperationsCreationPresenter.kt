package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.creation

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.exception.CashTransactionSavingException
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.domain.CashOperationsInteractor
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.navigation.CashOperationsRouter
import java.math.BigDecimal
import javax.inject.Inject

internal class CashOperationsCreationPresenter @Inject constructor(
    private val cashAmountLazyFlow: Lazy<Flow<BigDecimal>>,
    private val cashOperationLazyFlow: Lazy<Flow<CashOperation>>,
    private val cashOperationsInteractor: CashOperationsInteractor,
    private val cashOperationsRouter: CashOperationsRouter
) : MvpPresenter<CashOperationsCreationView>() {

    fun setOperation(value: CashOperation) =
        cashOperationsInteractor.setOperation(value)

    fun setTotalCash(value: String)=
        cashOperationsInteractor.setTotalCash(value)

    fun setMessage(value: String) =
        cashOperationsInteractor.setMessage(value)

    override fun onFirstViewAttach() {
        getCashOperations()
        getProvidedCashOperation()
        getProvidedCashAmount()
    }

    fun getCashOperations() {
        cashOperationsInteractor
            .getCashOperations()
            .launchCatchingIn(presenterScope)
            .onSuccess { viewState.onSuccessCashOperations(it) }
    }

    fun getAllowedCashAmount() {
        cashOperationsInteractor
            .getAllowedCashAmount()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCashAmount() }
            .onSuccess { cashAmount ->
                viewState
                    .onSuccessCashAmount(cashAmount, cashOperationsInteractor.getOperation())
            }
            .onFailure { viewState.onFailureCashAmount(it) }
    }

    fun getProvidedCashAmount() {
        cashAmountLazyFlow.get()
            .onEach { viewState.onCashAmountChanged(it) }
            .launchIn(presenterScope)
    }

    fun getProvidedCashOperation() {
        cashOperationLazyFlow.get()
            .onEach {
                viewState.onCashOperationChanged(it)
                getAllowedCashAmount()
            }
            .launchIn(presenterScope)
    }

    fun proceedSavingOperation() {
        cashOperationsInteractor.proceedSavingTransaction()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCashOperationValidation() }
            .onSuccess { viewState.onSuccessCashOperationValidation() }
            .onFailure {
                when (it) {
                    is CashTransactionSavingException -> {
                        if (it.isOperationNotDefined)
                            viewState.onFailureCashOperationSavingOperationNotDefined()
                        if (it.isOperationAmountNotValid)
                            viewState.onFailureCashOperationSavingAmountNotValid()
                    }
                    else -> viewState.onFailureCashOperationValidation(it)
                }
            }
    }

    fun showCashOperationProcessView() =
        viewState.onShowCashOperationProcessView()

    fun backToPreviousScreen() =
        cashOperationsRouter.backToCashOperationsDetailsScreen()
}