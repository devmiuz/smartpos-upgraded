package uz.uzkassa.smartpos.trade.auth.presentation

import moxy.MvpView

internal interface AuthView : MvpView {

    fun onAuthCodeDefined(code: String)

    fun onLoadingCodeCheck()

    fun onSuccessCodeCheck()

    fun onFailureCodeCheck(throwable: Throwable)
}