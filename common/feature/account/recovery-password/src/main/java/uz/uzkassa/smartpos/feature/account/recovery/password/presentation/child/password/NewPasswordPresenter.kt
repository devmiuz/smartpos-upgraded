package uz.uzkassa.smartpos.feature.account.recovery.password.presentation.child.password

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.account.recovery.password.domain.RecoveryPasswordInteractor
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.navigation.RecoveryPasswordRouter
import javax.inject.Inject

internal class NewPasswordPresenter @Inject constructor(
    private val recoveryPasswordInteractor: RecoveryPasswordInteractor,
    private val recoveryPasswordRouter: RecoveryPasswordRouter
) : MvpPresenter<NewPasswordView>() {
    private var isValidPassword: Boolean = false
    private var isPasswordConfirmed: Boolean = false

    override fun onFirstViewAttach() =
        viewState.onFinishAllowed(false)

    fun togglePasswordVisibility(isPasswordInput: Boolean) =
        viewState.onTogglePasswordVisibility(isPasswordInput)

    fun setPassword(password: String) {
        recoveryPasswordInteractor.setPassword(password)
            .onEach {
                isValidPassword = it.isValid
                viewState.onPasswordValidationChanged(it)
            }
            .launchIn(presenterScope)
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun checkPassword(password: String) {
        recoveryPasswordInteractor.checkPassword(password)
            .onStart {
                isPasswordConfirmed = false
                viewState.onFinishAllowed(false)
            }
            .onEach {
                isPasswordConfirmed = it
                if (it) viewState.apply { onHidePasswordError(); onFinishAllowed(isValidPassword) }
                else viewState.onShowPasswordErrorNotCheck()
            }
            .launchIn(presenterScope)
    }

    fun finishRecovery() {
        val isFinishAllowed: Boolean = isValidPassword && isPasswordConfirmed
        if (isFinishAllowed)
            recoveryPasswordInteractor
                .finishRecovery()
                .launchCatchingIn(presenterScope)
                .onStart { viewState.onLoadingFinishRecovery() }
                .onSuccess { recoveryPasswordRouter.finishPasswordRecovery() }
                .onFailure { viewState.onErrorFinishRecovery(it) }
        else viewState.apply { onShowPasswordErrorNotDefined(); onFinishAllowed(isFinishAllowed) }
    }
}