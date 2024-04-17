package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.details

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.domain.CashOperationsDetailsInteractor
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.navigation.CashOperationsRouter
import javax.inject.Inject

internal class CashOperationsDetailsPresenter @Inject constructor(
    private val cashOperationsDetailsInteractor: CashOperationsDetailsInteractor,
    private val cashOperationsRouter: CashOperationsRouter
) : MvpPresenter<CashOperationsDetailsView>() {

    override fun onFirstViewAttach() =
        getCashOperationsDetails()

    fun getCashOperationsDetails() {
        cashOperationsDetailsInteractor.getCashOperationsDetails()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCashOperationsDetails() }
            .onSuccess { viewState.onSuccessCashOperationsDetails(it) }
            .onFailure { viewState.onFailureCashOperationsDetails(it) }
    }

    fun navigateToCashOperationsCreationScreen() =
        cashOperationsRouter.openCashOperationsCreationScreen()

    fun backToPreviousScreen() =
        cashOperationsRouter.exit()
}