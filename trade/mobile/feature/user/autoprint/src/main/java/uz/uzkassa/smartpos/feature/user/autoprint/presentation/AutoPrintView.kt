package uz.uzkassa.smartpos.feature.user.autoprint.presentation

import moxy.MvpView

internal interface AutoPrintView : MvpView {

    fun onLoading()

    fun onReceiptReceived()

    fun onError(t: Throwable)

}