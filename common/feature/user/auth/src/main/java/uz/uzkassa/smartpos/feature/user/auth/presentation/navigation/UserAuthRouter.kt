package uz.uzkassa.smartpos.feature.user.auth.presentation.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.auth.dependencies.UserAuthFeatureArgs
import uz.uzkassa.smartpos.feature.user.auth.dependencies.UserAuthFeatureCallback
import uz.uzkassa.smartpos.feature.user.auth.presentation.features.cashier.CashierAuthFragment
import uz.uzkassa.smartpos.feature.user.auth.presentation.features.supervisor.SupervisorAuthFragment
import uz.uzkassa.smartpos.feature.user.auth.presentation.features.unsupported.UnsupportedAuthFragment

@Suppress("NON_EXHAUSTIVE_WHEN")
internal class UserAuthRouter(
    private val userAuthFeatureArgs: UserAuthFeatureArgs,
    private val userAuthFeatureCallback: UserAuthFeatureCallback
) : Router() {

    fun openRootScreen(user: User) {
        when (user.userRole.type) {
            UserRole.Type.BRANCH_ADMIN, UserRole.Type.OWNER ->
                newRootScreen(Screens.SupervisorAuthScreen)
            UserRole.Type.CASHIER -> newRootScreen(Screens.CashierAuthScreen)
            else -> newRootScreen(Screens.UnsupportedAuthScreen)
        }
    }

    fun openRecoveryPasswordScreen(phoneNumber: String) =
        userAuthFeatureCallback.onOpenRecoveryPassword(phoneNumber)

    fun finishAuthScreen(userRoleType: UserRole.Type) {
        val userId: Long = userAuthFeatureArgs.userId
        when (userAuthFeatureArgs.userRoleType) {
            UserRole.Type.BRANCH_ADMIN, UserRole.Type.OWNER ->
                userAuthFeatureCallback.onOpenSupervisorUser(userId, userRoleType)
            UserRole.Type.CASHIER ->
                userAuthFeatureCallback.onOpenCashierUser(userId, userRoleType)
            else -> Unit
        }
    }

    override fun exit() =
        userAuthFeatureCallback.onBackFromUserAuth()

    object Screens {

        object CashierAuthScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CashierAuthFragment.newInstance()
        }

        object SupervisorAuthScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                SupervisorAuthFragment.newInstance()
        }

        object UnsupportedAuthScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                UnsupportedAuthFragment.newInstance()
        }
    }
}