package uz.uzkassa.smartpos.feature.user.settings.password.presentation

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

internal interface UserPasswordChangeView : MvpView {

    fun onTogglePasswordVisibility(isPasswordInputClicked: Boolean)

    fun onLoadingPasswordChanging()

    fun onSuccessPasswordChanging()

    fun onErrorPasswordChangingCauseOldPasswordNotValid()

    fun onErrorPasswordChangingCauseNewPasswordNotValid()

    fun onErrorPasswordChangingCauseIncorrectPassword()

    fun onErrorPasswordChanging(throwable: Throwable)

    @OneExecution
    fun onDismissView()
}