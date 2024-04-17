package uz.uzkassa.smartpos.feature.account.recovery.password.presentation.child.confirmation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.account.recovery.password.data.exception.WrongConfirmationCodeException
import uz.uzkassa.smartpos.feature.account.recovery.password.domain.RecoveryPasswordInteractor
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.navigation.RecoveryPasswordRouter
import javax.inject.Inject

internal class ConfirmationCodePresenter @Inject constructor(
    private val recoveryPasswordInteractor: RecoveryPasswordInteractor,
    private val recoveryPasswordRouter: RecoveryPasswordRouter
) : MvpPresenter<ConfirmationCodeView>() {

    override fun onFirstViewAttach() {
        requestRecovery()
        viewState.onPhoneNumberDefined(recoveryPasswordInteractor.getHiddenPhoneNumber())
        viewState.onConfirmationCodedDefined(false)
    }

    fun requestRecovery() {
        recoveryPasswordInteractor
            .requestRecovery()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingRequest() }
            .onSuccess { viewState.onSuccessRequest() }
            .onFailure { viewState.onErrorRequest(it) }
    }

    fun setConfirmationCode(value: String) {
        recoveryPasswordInteractor.setConfirmationCode(value)
            .onEach { viewState.onConfirmationCodedDefined(it) }
            .launchIn(presenterScope)
    }

    fun activateRecovery() {
        recoveryPasswordInteractor
            .activateRecovery()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingActivation() }
            .onSuccess {
                viewState.onSuccessActivation()
                recoveryPasswordRouter.openNewPasswordScreen()
            }
            .onFailure {
                when (it) {
                    is WrongConfirmationCodeException ->
                        viewState.onErrorActivationCauseWrongCode()
                    else -> viewState.onErrorActivation(it)
                }
            }
    }
}