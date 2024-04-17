package uz.uzkassa.smartpos.feature.account.recovery.password.presentation.navigation

import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainRouter
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerNavigator
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerScreen
import uz.uzkassa.smartpos.feature.account.recovery.password.dependencies.AccountRecoveryPasswordFeatureCallback

internal class RecoveryPasswordRouter(
    private val accountRecoveryPasswordFeatureCallback: AccountRecoveryPasswordFeatureCallback
) : PlainRouter<ViewPagerNavigator>(ViewPagerNavigator()) {

    fun backToRootScreen() =
        accountRecoveryPasswordFeatureCallback.onBackFromRecoveryPassword()

    fun openConfirmationCodeScreen() =
        navigateTo(Screens.ConfirmationCodeScreen)

    fun openNewPasswordScreen() =
        navigateTo(Screens.NewPasswordScreen)

    fun finishPasswordRecovery() =
        accountRecoveryPasswordFeatureCallback.onFinishRecoveryPassword()

    object Screens {

        object ConfirmationCodeScreen : ViewPagerScreen() {
            override fun getScreenPosition(): Int? = 1
        }

        object NewPasswordScreen : ViewPagerScreen() {
            override fun getScreenPosition(): Int? = 2
        }
    }
}