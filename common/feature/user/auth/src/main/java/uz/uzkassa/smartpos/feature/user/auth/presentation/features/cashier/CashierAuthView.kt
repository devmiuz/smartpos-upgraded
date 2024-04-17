package uz.uzkassa.smartpos.feature.user.auth.presentation.features.cashier

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

internal interface CashierAuthView : MvpView {

    fun onLoadingUser()

    fun onSuccessUser(user: User)

    fun onErrorUser(throwable: Throwable)

    fun onLoadingAuth()

    fun onErrorAuthCauseIncorrectPinCode(throwable: Throwable)

    fun onErrorAuth(throwable: Throwable)

    fun onShowRequestNewPinCodeAlert()

    fun onDismissNewPinCodeAlert()

    fun onLoadingRequestNewPinCode()

    fun onSuccessRequestNewPinCode()

    fun onErrorRequestNewPinCode(throwable: Throwable)
}