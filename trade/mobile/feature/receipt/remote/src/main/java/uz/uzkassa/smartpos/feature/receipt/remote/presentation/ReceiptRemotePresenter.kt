package uz.uzkassa.smartpos.feature.receipt.remote.presentation

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.feature.receipt.remote.domain.ReceiptRemoteInteractor
import javax.inject.Inject

internal class ReceiptRemotePresenter @Inject constructor(
    private val receiptRemoteInteractor: ReceiptRemoteInteractor
) : MvpPresenter<ReceiptRemoteView>() {
    private var isAlertDismissed: Boolean = true

    fun dismissReceivedReceiptRemoteAlert() {
        viewState.onDismissReceivedReceiptRemoteAlert()
        isAlertDismissed = true
    }

//    private fun subscribe() {
//        receiptRemoteInteractor
//            .subscribe()
//            .filter { isAlertDismissed }
//            .onEach { isAlertDismissed = false; viewState.onShowReceivedReceiptRemoteAlert() }
//            .launchIn(presenterScope)
//    }
}