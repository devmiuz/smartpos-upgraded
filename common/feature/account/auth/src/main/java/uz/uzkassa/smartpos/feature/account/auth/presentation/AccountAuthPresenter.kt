package uz.uzkassa.smartpos.feature.account.auth.presentation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.account.auth.dependencies.AccountAuthFeatureCallback
import uz.uzkassa.smartpos.feature.account.auth.domain.AccountAuthInteractor
import javax.inject.Inject

internal class AccountAuthPresenter @Inject constructor(
    private val accountAuthFeatureCallback: AccountAuthFeatureCallback,
    private val accountAuthInteractor: AccountAuthInteractor
) : MvpPresenter<AccountAuthView>() {
    private var isPhoneNumberInputted: Boolean = false

    override fun onFirstViewAttach() {
        viewState.onPhoneNumberChanged(false)
        viewState.onAuthAllowed(false)
    }

    fun setPhoneNumber(value: String) {
        accountAuthInteractor.setPhoneNumber(value)
            .onEach {
                isPhoneNumberInputted = it
                viewState.onPhoneNumberChanged(it)
            }
            .launchIn(presenterScope)
    }

    fun setPassword(password: String) {
        accountAuthInteractor.setPassword(password)
            .onEach { viewState.onAuthAllowed(it.isValid && isPhoneNumberInputted) }
            .launchIn(presenterScope)
    }

    fun authenticate() {
        accountAuthInteractor.authenticate()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingAuth() }
            .onSuccess {
                viewState.onSuccessAuth()
                accountAuthFeatureCallback.onFinishAccountAuth(it)
            }
            .onFailure { viewState.onErrorAuth(it) }
    }

    fun requestPasswordRecovery() {
        accountAuthInteractor.requestPasswordRecovery()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingRequestPasswordRecovery() }
            .onSuccess {
                viewState.onSuccessRequestPasswordRecovery()
                accountAuthFeatureCallback.onOpenAccountRecoveryPassword(it)
            }
            .onFailure { viewState.onErrorAuth(it) }
    }

    fun backToRootScreen() =
        accountAuthFeatureCallback.onBackFromAccountAuth()
}