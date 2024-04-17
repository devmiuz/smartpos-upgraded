package uz.uzkassa.smartpos.feature.user.settings.phonenumber.presentation

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

internal interface UserPhoneNumberChangeView : MvpView {

    fun onLoadingPhoneNumberChange()

    fun onSuccessPhoneNumberChange()

    fun onErrorPhoneNumberChangeCausePhoneNumberNotValid()

    fun onErrorPhoneNumberChangeCauseUserWithEnteredPhoneNumberAlreadyExists()

    fun onErrorPhoneNumberChange(throwable: Throwable)

    @OneExecution
    fun onDismissView()
}