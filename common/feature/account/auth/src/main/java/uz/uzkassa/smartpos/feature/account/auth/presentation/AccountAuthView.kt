package uz.uzkassa.smartpos.feature.account.auth.presentation

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

interface AccountAuthView : MvpView {

    @OneExecution
    fun onPhoneNumberChanged(isInputted: Boolean)

    fun onAuthAllowed(isAllowed: Boolean)

    fun onLoadingAuth()

    fun onSuccessAuth()

    fun onLoadingRequestPasswordRecovery()

    fun onSuccessRequestPasswordRecovery()

    fun onErrorAuth(throwable: Throwable)
}