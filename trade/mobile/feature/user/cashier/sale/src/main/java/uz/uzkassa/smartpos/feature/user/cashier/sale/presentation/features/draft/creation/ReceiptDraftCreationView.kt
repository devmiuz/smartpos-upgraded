package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.creation

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.receipt.ReceiptDraftDetails

internal interface ReceiptDraftCreationView : MvpView {

    fun onReceiptDraftCreationDetailsDefined(details: ReceiptDraftDetails)

    fun onLoadingReceiptDraftCreation()

    fun onSuccessReceiptDraftCreation()

    fun onErrorReceiptDraftCreationCauseNameNotInputted()

    fun onErrorReceiptDraftCreation(throwable: Throwable)

    fun onErrorProductMarkingSaving(throwable: Throwable)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onDismissView()
}