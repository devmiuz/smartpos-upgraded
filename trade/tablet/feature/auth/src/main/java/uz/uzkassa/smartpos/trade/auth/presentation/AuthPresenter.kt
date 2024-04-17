package uz.uzkassa.smartpos.trade.auth.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.trade.auth.dependencies.AuthFeatureCallback
import uz.uzkassa.smartpos.trade.auth.domain.AuthInteractor
import javax.inject.Inject

internal class AuthPresenter @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val authFeatureCallback: AuthFeatureCallback
) : MvpPresenter<AuthView>() {

    override fun onFirstViewAttach() =
        getDefinedAuthCode()

    fun getDefinedAuthCode() {
        authInteractor
            .getAuthCode()
            .launchCatchingIn(presenterScope)
            .onSuccess { code ->
                viewState.onAuthCodeDefined(code)
                checkCode()
            }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun checkCode() {
        authInteractor
            .checkCode()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCodeCheck() }
            .onSuccess {
                viewState.onSuccessCodeCheck()
                authFeatureCallback.onFinish()
            }
            .onFailure { viewState.onFailureCodeCheck(it) }
    }
}