package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.list

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarking
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraft
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout.DrawerStateDelegate
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.receipt.draft.list.ReceiptDraftListInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.receipt.draft.restore.ReceiptDraftRestoreInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter
import javax.inject.Inject

internal class ReceiptDraftListPresenter @Inject constructor(
    private val drawerStateDelegate: DrawerStateDelegate,
    private val receiptDraftListInteractor: ReceiptDraftListInteractor,
    private val receiptDraftRestoreInteractor: ReceiptDraftRestoreInteractor,
    private val cashierSaleRouter: CashierSaleRouter
) : MvpPresenter<ReceiptDraftListView>() {
    var job: Job? = null

    @ExperimentalCoroutinesApi
    override fun onFirstViewAttach() {
        drawerStateDelegate.setLockState(true)
        getReceiptDrafts()
    }

    override fun detachView(view: ReceiptDraftListView?) {
        super.detachView(view)
        job?.cancel()
        job = null
    }

    @ExperimentalCoroutinesApi
    fun getReceiptDrafts(search: String? = null) {
        if (job != null) {
            job?.cancel()
        }
        job = presenterScope.launch {
            receiptDraftListInteractor
                .getReceiptDrafts(search)
                .onStart {
                    viewState.onLoadingReceiptDrafts()
                }.collect {
                    viewState.onSuccessReceiptDrafts(it.getOrThrow())
                }
        }

    }

    fun selectReceiptDraftForRestore(receiptDraft: ReceiptDraft) {
        if (receiptDraftRestoreInteractor.hasDataForRestore)
            viewState.onShowRestoreAlert(receiptDraft.id, receiptDraft.name)
        else restoreReceiptDraft(receiptDraft.id)
    }

    fun showReceiptDraftCreation() =
        viewState.let { it.onDismissAlert(); it.onShowReceiptDraftCreationView() }

    fun selectReceiptDraftForDelete(draft: ReceiptDraft) =
        viewState.onShowDeleteAlert(draft)

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun restoreReceiptDraft(id: Long) {
        receiptDraftRestoreInteractor
            .restoreReceiptDraftById(id)
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingRestore() }
            .onSuccess { viewState.onSuccessRestore(); backToRootScreen() }
            .onFailure { viewState.onErrorRestore(it) }
    }

    fun deleteReceiptDraft(receiptDraft: ReceiptDraft) {
        receiptDraftListInteractor
            .deleteReceiptDraftById(receiptDraft)
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingDelete() }
            .onSuccess { deleteMarkings(receiptDraft) }
            .onFailure { viewState.onErrorDelete(it) }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun deleteMarkings(receiptDraft: ReceiptDraft) {
        receiptDraftListInteractor
            .deleteProductMarkings(receiptDraft.receipt)
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingDelete() }
            .onSuccess {
                viewState.onSuccessDelete(receiptDraft)
            }
            .onFailure { viewState.onErrorDelete(it) }
    }

    fun dismissAlert() =
        viewState.onDismissAlert()

    fun backToRootScreen() =
        cashierSaleRouter.backToTabScreen()

    override fun onDestroy() =
        drawerStateDelegate.setLockState(false)
}