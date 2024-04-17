package uz.uzkassa.apay.presentation.features.phone

import moxy.MvpView

internal interface PhoneApayView : MvpView {

    fun onTick(progress: Int, time: String)

    fun onLoading()

    fun onSuccess()

    fun onError(it: Throwable)

    fun openCheckPayDialog()

    fun onUpdateBillSuccess()
}