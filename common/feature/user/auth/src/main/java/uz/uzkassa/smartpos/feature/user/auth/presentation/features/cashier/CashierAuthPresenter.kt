package uz.uzkassa.smartpos.feature.user.auth.presentation.features.cashier

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.auth.data.exceptions.IncorrectPinCodeException
import uz.uzkassa.smartpos.feature.user.auth.domain.cashier.UserAuthCashierInteractor
import uz.uzkassa.smartpos.feature.user.auth.presentation.navigation.UserAuthRouter
import javax.inject.Inject

internal class CashierAuthPresenter @Inject constructor(
    private val userAuthCashierInteractor: UserAuthCashierInteractor,
    private val userAuthRouter: UserAuthRouter
) : MvpPresenter<CashierAuthView>() {

    override fun onFirstViewAttach() =
        getUser()

    fun getUser() {
        userAuthCashierInteractor
            .getUser()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUser() }
            .onSuccess { viewState.onSuccessUser(it) }
            .onFailure { viewState.onErrorUser(it) }
    }

    fun authenticate(pinCode: String) {
        userAuthCashierInteractor
            .authenticate(pinCode)
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingAuth() }
            .onSuccess { userAuthRouter.finishAuthScreen(it) }
            .onFailure {
                when (it) {
                    is IncorrectPinCodeException ->
                        viewState.onErrorAuthCauseIncorrectPinCode(it)
                    else -> viewState.onErrorAuth(it)
                }
            }
    }

    fun showRequestNewPinCodeAlert(show: Boolean) {
        if (show) viewState.onShowRequestNewPinCodeAlert()
        else viewState.onDismissNewPinCodeAlert()
    }

    fun requestNewPinCode(dismissAlert: Boolean = false) {
        if (dismissAlert) viewState.onDismissNewPinCodeAlert()
        userAuthCashierInteractor
            .requestNewPinCode()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingRequestNewPinCode() }
            .onSuccess { viewState.onSuccessRequestNewPinCode() }
            .onFailure { viewState.onErrorRequestNewPinCode(it) }
    }
}