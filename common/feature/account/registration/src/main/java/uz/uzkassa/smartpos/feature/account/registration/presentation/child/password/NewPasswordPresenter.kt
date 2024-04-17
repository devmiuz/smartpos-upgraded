package uz.uzkassa.smartpos.feature.account.registration.presentation.child.password

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.feature.account.registration.domain.RegistrationInteractor
import uz.uzkassa.smartpos.feature.account.registration.presentation.navigation.RegistrationRouter
import javax.inject.Inject

internal class NewPasswordPresenter @Inject constructor(
    private val registrationInteractor: RegistrationInteractor,
    private val registrationRouter: RegistrationRouter
) : MvpPresenter<NewPasswordView>() {
    private var isValidPassword: Boolean = false
    private var isPasswordConfirmed: Boolean = false

    override fun onFirstViewAttach() {
        viewState.onAllValidationsNotMatch()
        viewState.onFinishAllowed(false)
    }

    fun togglePasswordVisibility(isPasswordInput: Boolean) =
        viewState.onTogglePasswordVisibility(isPasswordInput)

    fun setPassword(password: String) {
        registrationInteractor.setPassword(password)
            .onEach {
                isValidPassword = it.isValid
                viewState.onPasswordValidationChanged(it)
            }
            .launchIn(presenterScope)
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun checkPassword(password: String) {
        registrationInteractor.checkPassword(password)
            .onStart {
                isPasswordConfirmed = false
                viewState.onFinishAllowed(false)
            }
            .onEach {
                isPasswordConfirmed = it
                if (it) viewState.apply {
                    onHidePasswordError(); onFinishAllowed(isValidPassword)
                }
                else viewState.onShowPasswordErrorNotCheck()
            }
            .launchIn(presenterScope)
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun proceedContinue() {
        val isProceedEnable: Boolean = isValidPassword && isPasswordConfirmed
        if (isProceedEnable)
            registrationInteractor.finishAccountRegistration()
                .onStart { viewState.onLoadingFinishRegistration() }
                .onEach { registrationRouter.finishRegistration() }
                .catch { viewState.onErrorFinishRegistration(it) }
                .launchIn(presenterScope)
        else viewState.apply {
            onShowPasswordErrorNotDefined(); onFinishAllowed(isProceedEnable)
        }
    }

    fun backToTermsOfUseScreen() =
        registrationRouter.openTermsOfUse()
}