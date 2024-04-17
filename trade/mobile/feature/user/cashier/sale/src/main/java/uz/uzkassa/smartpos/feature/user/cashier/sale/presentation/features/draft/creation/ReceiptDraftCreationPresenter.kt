package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.creation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.exception.receipt.ReceiptDraftNameNotInputtedException
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.receipt.draft.creation.ReceiptDraftCreationInteractor
import javax.inject.Inject

internal class ReceiptDraftCreationPresenter @Inject constructor(
    private val receiptDraftCreationInteractor: ReceiptDraftCreationInteractor
) : MvpPresenter<ReceiptDraftCreationView>() {

    override fun onFirstViewAttach() {
        with(receiptDraftCreationInteractor.getReceiptDraftDetails()) {
            viewState.onReceiptDraftCreationDetailsDefined(this)
        }
    }

    fun setDraftName(receiptName: String) =
        receiptDraftCreationInteractor.setDraftName(receiptName)

    fun setCustomerName(name: String) =
        receiptDraftCreationInteractor.setCustomerName(name)

    fun setCustomerContact(contact: String) =
        receiptDraftCreationInteractor.setCustomerContact(contact)

    fun clearSaleData() = receiptDraftCreationInteractor.clearSaleData()

    fun proceed() {
        receiptDraftCreationInteractor
            .createReceiptDraft()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingReceiptDraftCreation() }
            .onSuccess {
                saveMarkings()
            }
            .onFailure {
                when (it) {
                    is ReceiptDraftNameNotInputtedException ->
                        viewState.onErrorReceiptDraftCreationCauseNameNotInputted()
                    else -> viewState.onErrorReceiptDraftCreation(it)
                }
            }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun saveMarkings() {
        receiptDraftCreationInteractor
            .saveProductMarkings()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingReceiptDraftCreation() }
            .onSuccess {
                viewState.apply {
                    onSuccessReceiptDraftCreation()
                    onDismissView()
                }
            }
            .onFailure { viewState.onErrorProductMarkingSaving(it) }
    }

    fun dismiss() =
        viewState.onDismissView()
}