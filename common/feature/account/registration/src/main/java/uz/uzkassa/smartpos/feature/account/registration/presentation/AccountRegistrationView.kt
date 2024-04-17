package uz.uzkassa.smartpos.feature.account.registration.presentation

import moxy.MvpView

internal interface AccountRegistrationView : MvpView {

    fun onTermsOfUseScreenVisible()

    fun onConfirmationCodeScreenVisible()

    fun onPasswordScreenVisible()
}