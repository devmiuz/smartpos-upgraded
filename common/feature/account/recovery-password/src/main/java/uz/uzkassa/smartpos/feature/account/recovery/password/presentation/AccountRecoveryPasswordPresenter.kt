package uz.uzkassa.smartpos.feature.account.recovery.password.presentation

import moxy.MvpPresenter
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainNavigationScreen
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainRouter
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.navigation.RecoveryPasswordRouter
import uz.uzkassa.smartpos.feature.account.recovery.password.presentation.navigation.RecoveryPasswordRouter.Screens
import javax.inject.Inject

internal class AccountRecoveryPasswordPresenter @Inject constructor(
    private val recoveryPasswordRouter: RecoveryPasswordRouter
) : MvpPresenter<AccountRecoveryPasswordView>(), PlainRouter.NavigatorObserver {

    override fun onFirstViewAttach() {
        recoveryPasswordRouter.let {
            it.addNavigatorObserver(this)
            it.openConfirmationCodeScreen()
        }
    }

    override fun onObserveNavigation(screen: PlainNavigationScreen) {
        when (screen) {
            is Screens.ConfirmationCodeScreen ->
                viewState.onConfirmationCodeScreenVisible()
            is Screens.NewPasswordScreen ->
                viewState.onNewPasswordScreenVisible()
        }
    }

    fun backToRoot() =
        recoveryPasswordRouter.backToRootScreen()

    override fun onDestroy() =
        recoveryPasswordRouter.removeNavigatorObserver(this)
}