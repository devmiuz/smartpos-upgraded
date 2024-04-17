package uz.uzkassa.smartpos.feature.user.auth.presentation.features.supervisor

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.auth.data.exceptions.IncorrectPasswordException
import uz.uzkassa.smartpos.feature.user.auth.data.exceptions.PasswordNotDefinedException
import uz.uzkassa.smartpos.feature.user.auth.domain.supervisor.SupervisorAuthInteractor
import uz.uzkassa.smartpos.feature.user.auth.presentation.navigation.UserAuthRouter
import javax.inject.Inject

internal class SupervisorAuthPresenter @Inject constructor(
    private val userAuthRouter: UserAuthRouter,
    private val supervisorAuthInteractor: SupervisorAuthInteractor
) : MvpPresenter<SupervisorAuthView>() {

    override fun onFirstViewAttach() {
        getUser()
        viewState.onPasswordDefined(false)
    }

    fun getUser() {
        supervisorAuthInteractor
            .getUser()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUser() }
            .onSuccess { viewState.onSuccessUser(it) }
            .onFailure { viewState.onErrorUser(it) }
    }

    fun setPassword(password: String) {
        supervisorAuthInteractor
            .setPassword(password)
            .onEach { viewState.onPasswordDefined(it) }
            .launchIn(presenterScope)
    }

    fun authenticate() {
        supervisorAuthInteractor
            .authenticate()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingAuth() }
            .onSuccess { userAuthRouter.finishAuthScreen(it) }
            .onFailure {
                when (it) {
                    is PasswordNotDefinedException ->
                        viewState.onErrorAuthCausePasswordNotDefined()
                    is IncorrectPasswordException ->
                        viewState.onErrorAuthCauseInvalidCredentials()
                    else -> viewState.onErrorAuth(it)
                }
            }
    }

    fun openRecoveryPasswordScreen() {
        supervisorAuthInteractor.getUserPhoneNumber().let {
            userAuthRouter.openRecoveryPasswordScreen(it)
        }
    }
}