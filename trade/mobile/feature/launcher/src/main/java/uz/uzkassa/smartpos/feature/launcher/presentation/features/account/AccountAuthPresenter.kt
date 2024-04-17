package uz.uzkassa.smartpos.feature.launcher.presentation.features.account

import moxy.MvpPresenter
import uz.uzkassa.smartpos.feature.launcher.domain.account.AccountAuthInteractor
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.LauncherRouter
import javax.inject.Inject

@Suppress("unused")
internal class AccountAuthPresenter @Inject constructor(
    private val accountAuthInteractor: AccountAuthInteractor,
    private val launcherRouter: LauncherRouter
) : MvpPresenter<AccountAuthView>() {

    fun openAccountAuthLoginScreen() =
        launcherRouter.openAccountAuthScreen(false)

    fun openAccountAuthRegistrationScreen() =
        launcherRouter.openAccountAuthRegistrationScreen()
}