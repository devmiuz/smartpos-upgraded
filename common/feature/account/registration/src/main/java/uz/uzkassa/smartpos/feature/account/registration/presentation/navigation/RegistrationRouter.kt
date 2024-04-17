package uz.uzkassa.smartpos.feature.account.registration.presentation.navigation

import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainRouter
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerNavigator
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerScreen
import uz.uzkassa.smartpos.feature.account.registration.dependencies.AccountRegistrationFeatureCallback

internal class RegistrationRouter(
    private val accountRegistrationFeatureCallback: AccountRegistrationFeatureCallback
) : PlainRouter<ViewPagerNavigator>(ViewPagerNavigator()) {

    fun backToRoot() =
        accountRegistrationFeatureCallback.onBackFromRegistration()

    fun openTermsOfUse() =
        navigateTo(Screens.TermsOfUseScreen)

    fun openConfirmationCode() =
        navigateTo(Screens.ConfirmationCode)

    fun openNewPassword() =
        navigateTo(Screens.NewPassword)

    fun finishRegistration() =
        accountRegistrationFeatureCallback.onFinishRegistration()

    object Screens {

        object ConfirmationCode : ViewPagerScreen() {
            override fun getScreenPosition(): Int? = 2
        }

        object NewPassword : ViewPagerScreen() {
            override fun getScreenPosition(): Int? = 3
        }

        object TermsOfUseScreen : ViewPagerScreen() {
            override fun getScreenPosition(): Int? = 1
        }
    }
}