package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation

import moxy.MvpPresenter
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.creation.CashOperationsCreationView
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.navigation.CashOperationsRouter
import javax.inject.Inject

internal class CashOperationsPresenter @Inject constructor(
    private val cashOperationsRouter: CashOperationsRouter
) : MvpPresenter<CashOperationsCreationView>() {

    override fun onFirstViewAttach() =
        cashOperationsRouter.openCashOperationsDetailsScreen()
}