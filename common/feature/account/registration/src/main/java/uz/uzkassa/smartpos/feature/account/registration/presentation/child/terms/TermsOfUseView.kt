package uz.uzkassa.smartpos.feature.account.registration.presentation.child.terms

import moxy.MvpView

internal interface TermsOfUseView : MvpView {

    fun onSettingPhoneNumber(phoneNumber: String)

    fun onDataChanging(isPhoneNumberDefined: Boolean, isTermsOfUseChecked: Boolean)

    fun onShowConfirmationAlert()

    fun onDismissConfirmationAlert()
}