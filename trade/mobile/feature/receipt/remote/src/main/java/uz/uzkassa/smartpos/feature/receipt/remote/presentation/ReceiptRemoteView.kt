package uz.uzkassa.smartpos.feature.receipt.remote.presentation

import moxy.MvpView

internal interface ReceiptRemoteView : MvpView {

    fun onShowReceivedReceiptRemoteAlert()

    fun onDismissReceivedReceiptRemoteAlert()
}