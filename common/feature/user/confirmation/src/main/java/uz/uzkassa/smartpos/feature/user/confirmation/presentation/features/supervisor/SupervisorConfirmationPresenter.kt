package uz.uzkassa.smartpos.feature.user.confirmation.presentation.features.supervisor

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.utils.password.PasswordValidation
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.confirmation.data.exception.InvalidConfirmationCredentialsException
import uz.uzkassa.smartpos.feature.user.confirmation.data.exception.PasswordNotInputtedException
import uz.uzkassa.smartpos.feature.user.confirmation.domain.SupervisorConfirmationInteractor
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.navigation.UserConfirmationRouter
import javax.inject.Inject

internal class SupervisorConfirmationPresenter @Inject constructor(
    private val supervisorConfirmationInteractor: SupervisorConfirmationInteractor,
    private val userConfirmationRouter: UserConfirmationRouter
) : MvpPresenter<SupervisorConfirmationView>() {

    override fun onFirstViewAttach() {
        viewState.onUserRoleTypeDefined(supervisorConfirmationInteractor.getUserRoleType())
        viewState.onPasswordChanged(false)
    }

    fun setPassword(password: String) {
        supervisorConfirmationInteractor
            .setPassword(password)
            .onEach { viewState.onPasswordChanged(it) }
            .launchIn(presenterScope)
    }

    fun confirmSupervisor() {
        supervisorConfirmationInteractor
            .confirmSupervisor()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingConfirmation() }
            .onSuccess { userConfirmationRouter.finish() }
            .onFailure {
                when (it) {
                    is PasswordNotInputtedException ->
                        viewState.onErrorConfirmationCausePasswordNotDefined()
                    is PasswordValidation.Exception ->
                        viewState.onErrorConfirmationCausePasswordNotValid()
                    is InvalidConfirmationCredentialsException ->
                        viewState.onErrorConfirmationCauseUnauthorized()
                    else -> viewState.onErrorConfirmation(it)
                }
            }
    }

    fun backToRootScreen() =
        userConfirmationRouter.backToRootScreen()
}