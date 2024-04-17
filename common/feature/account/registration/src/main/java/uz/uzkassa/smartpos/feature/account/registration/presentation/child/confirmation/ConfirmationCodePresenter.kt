package uz.uzkassa.smartpos.feature.account.registration.presentation.child.confirmation

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainNavigationScreen
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainRouter
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.account.registration.data.exception.ResendCodeNotAvailableException
import uz.uzkassa.smartpos.feature.account.registration.data.exception.UserAlreadyExistsException
import uz.uzkassa.smartpos.feature.account.registration.domain.RegistrationConfirmationInteractor
import uz.uzkassa.smartpos.feature.account.registration.domain.RegistrationInteractor
import uz.uzkassa.smartpos.feature.account.registration.presentation.navigation.RegistrationRouter
import javax.inject.Inject

internal class ConfirmationCodePresenter @Inject constructor(
    private val registrationConfirmationInteractor: RegistrationConfirmationInteractor,
    private val registrationInteractor: RegistrationInteractor,
    private val registrationRouter: RegistrationRouter
) : MvpPresenter<ConfirmationCodeView>(), PlainRouter.NavigatorObserver {
    private var isInitCountdownCalled: Boolean = false
    private var isResendCodeAvailable: Boolean = true

    private var isRequestActivationError: Boolean? = null
    private var isRequestActivateRegistrationError: Boolean? = null

    override fun onFirstViewAttach() {
        registrationRouter.addNavigatorObserver(this)
        viewState.apply {
            onProceedAllowed(false)
            onCountdownStarted()
        }
    }

    override fun onObserveNavigation(screen: PlainNavigationScreen) {
        if (screen is RegistrationRouter.Screens.ConfirmationCode) startCountdown()
        else stopCountdown()
    }

    private fun startCountdown() {
        if (!isInitCountdownCalled) {
            requestActivationCode()
            isInitCountdownCalled = true
        }
    }

    private fun stopCountdown() {
        isInitCountdownCalled = false
        viewState.onResetView()
        presenterScope.coroutineContext.cancelChildren(null)
    }

    fun requestActivationCode() {
        registrationInteractor
            .requestConfirmationSmsCode()
            .launchCatchingIn(presenterScope)
            .onStart {
                isRequestActivationError = null
                viewState.onLoadingRequestConfirmationCode()
            }
            .onSuccess {
                isResendCodeAvailable = it.isResendAvailable
                viewState.onSuccessRequestConfirmationCode(it)
                proceedCountdown()
            }
            .onFailure {
                when (it) {
                    is ResendCodeNotAvailableException ->
                        viewState.onErrorRequestCauseResendNotAvailable()
                    is UserAlreadyExistsException -> {
                        registrationRouter.openTermsOfUse()
                        viewState.onErrorRequestConfirmationCode(it)
                    }
                    else -> {
                        isResendCodeAvailable = true
                        viewState.onErrorRequestConfirmationCode(it)
                    }
                }
            }
    }

    fun setConfirmationCode(code: String) {
        registrationConfirmationInteractor.setActivationCode(code)
            .onEach {
                if (it) registrationInteractor.setConfirmationCode(code)
                viewState.onProceedAllowed(it)
            }
            .launchIn(presenterScope)
    }

    fun proceedContinue() {
        registrationInteractor
            .activateAccountRegistrationBySmsCode()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingActivateRegistration() }
            .onSuccess {
                viewState.onSuccessActivateRegistration()
                registrationRouter.openNewPassword()
            }
            .onFailure { viewState.onErrorActivateRegistration(it) }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    private fun proceedCountdown() {
        viewState.onCountdownStarted()
        registrationConfirmationInteractor.requestCountdownForResend()
            .onEach {
                viewState.onProceedCountdown(it.period.toInt(), it.countdown.toInt())
                if (it.isCompleted) viewState.onCountdownCompleted(isResendCodeAvailable)
            }
            .launchIn(presenterScope)
    }

    override fun onDestroy() =
        registrationRouter.removeNavigatorObserver(this)
}