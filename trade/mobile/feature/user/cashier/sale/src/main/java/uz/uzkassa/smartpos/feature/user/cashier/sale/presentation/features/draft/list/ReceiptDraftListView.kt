package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.list

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraft

internal interface ReceiptDraftListView : MvpView {

    fun onLoadingReceiptDrafts()

    fun onSuccessReceiptDrafts(receiptDrafts: List<ReceiptDraft>)

    fun onErrorReceiptDrafts(throwable: Throwable)

    fun onShowRestoreAlert(receiptId: Long, receiptName: String)

    @OneExecution
    fun onShowReceiptDraftCreationView()

    fun onLoadingRestore()

    fun onSuccessRestore()

    fun onErrorRestore(throwable: Throwable)

    fun onShowDeleteAlert(receiptDraft: ReceiptDraft)

    fun onLoadingDelete()

    fun onSuccessDelete(receiptDraft: ReceiptDraft)

    fun onErrorDelete(throwable: Throwable)

    fun onDismissAlert()
}