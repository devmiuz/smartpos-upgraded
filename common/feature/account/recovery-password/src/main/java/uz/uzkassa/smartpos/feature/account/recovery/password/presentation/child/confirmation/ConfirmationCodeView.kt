package uz.uzkassa.smartpos.feature.account.recovery.password.presentation.child.confirmation

import moxy.MvpView

internal interface ConfirmationCodeView : MvpView {

    fun onPhoneNumberDefined(phoneNumber: String)

    fun onConfirmationCodedDefined(isAccepted: Boolean)

    fun onLoadingRequest()

    fun onSuccessRequest()

    fun onErrorRequest(throwable: Throwable)

    fun onLoadingActivation()

    fun onSuccessActivation()

    fun onErrorActivationCauseWrongCode()

    fun onErrorActivation(throwable: Throwable)
}