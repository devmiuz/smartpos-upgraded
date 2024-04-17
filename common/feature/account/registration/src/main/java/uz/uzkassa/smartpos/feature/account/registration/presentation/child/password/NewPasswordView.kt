package uz.uzkassa.smartpos.feature.account.registration.presentation.child.password

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.utils.password.PasswordValidation

internal interface NewPasswordView : MvpView {


    fun onAllValidationsNotMatch()

    fun onTogglePasswordVisibility(isPasswordInputClicked: Boolean)

    fun onPasswordValidationChanged(validation: PasswordValidation)

    fun onFinishAllowed(isAllowed: Boolean)

    fun onShowPasswordErrorNotDefined()

    fun onShowPasswordErrorNotCheck()

    fun onHidePasswordError()

    fun onLoadingFinishRegistration()

    fun onErrorFinishRegistration(throwable: Throwable)
}