package uz.uzkassa.smartpos.feature.account.recovery.password.presentation

import moxy.MvpView

internal interface AccountRecoveryPasswordView : MvpView {

    fun onConfirmationCodeScreenVisible()

    fun onNewPasswordScreenVisible()
}