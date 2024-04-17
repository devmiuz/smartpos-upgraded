package uz.uzkassa.smartpos.feature.account.registration.presentation

import moxy.MvpPresenter
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainNavigationScreen
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainRouter
import uz.uzkassa.smartpos.feature.account.registration.presentation.navigation.RegistrationRouter
import uz.uzkassa.smartpos.feature.account.registration.presentation.navigation.RegistrationRouter.Screens
import javax.inject.Inject

internal class AccountRegistrationPresenter @Inject constructor(
    private val registrationRouter: RegistrationRouter
) : MvpPresenter<AccountRegistrationView>(), PlainRouter.NavigatorObserver {

    public override fun onFirstViewAttach() =
        registrationRouter.let {
            it.addNavigatorObserver(this)
            it.openTermsOfUse()
        }

    override fun onObserveNavigation(screen: PlainNavigationScreen) {
        when (screen) {
            Screens.TermsOfUseScreen ->
                viewState.onTermsOfUseScreenVisible()
            Screens.ConfirmationCode ->
                viewState.onConfirmationCodeScreenVisible()
            Screens.NewPassword ->
                viewState.onPasswordScreenVisible()
        }
    }

    fun backToRoot() =
        registrationRouter.backToRoot()

    override fun onDestroy() =
        registrationRouter.removeNavigatorObserver(this)
}