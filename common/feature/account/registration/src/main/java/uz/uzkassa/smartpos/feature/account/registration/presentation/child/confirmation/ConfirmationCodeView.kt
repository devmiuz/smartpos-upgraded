package uz.uzkassa.smartpos.feature.account.registration.presentation.child.confirmation

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.feature.account.registration.data.model.code.ConfirmationCode

internal interface ConfirmationCodeView : MvpView {

    @OneExecution
    fun onResetView()

    fun onLoadingRequestConfirmationCode()

    fun onSuccessRequestConfirmationCode(code: ConfirmationCode)

    fun onErrorRequestCauseResendNotAvailable()

    fun onErrorRequestConfirmationCode(throwable: Throwable)

    fun onProceedAllowed(allowed: Boolean)

    fun onCountdownStarted()

    fun onProceedCountdown(period: Int, countdown: Int)

    fun onCountdownCompleted(isResendCodeAvailable: Boolean)

    fun onLoadingActivateRegistration()

    fun onSuccessActivateRegistration()

    fun onErrorActivateRegistration(throwable: Throwable)
}