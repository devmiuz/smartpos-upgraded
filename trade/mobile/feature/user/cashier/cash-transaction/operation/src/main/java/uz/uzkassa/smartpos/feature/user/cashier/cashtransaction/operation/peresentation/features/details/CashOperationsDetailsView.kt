package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.details

import moxy.MvpView
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.details.CashOperationsDetails

internal interface CashOperationsDetailsView : MvpView {

    fun onLoadingCashOperationsDetails()

    fun onSuccessCashOperationsDetails(cashOperationsDetails: CashOperationsDetails)

    fun onFailureCashOperationsDetails(throwable: Throwable)
}