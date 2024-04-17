package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies.SupervisorDashboardFeatureArgs
import uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies.SupervisorDashboardFeatureCallback
import uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.features.sales.SalesDynamicsFragment

class SupervisorDashboardRouter(
    private val router: Router,
    supervisorDashboardFeatureArgs: SupervisorDashboardFeatureArgs,
    private val supervisorDashboardFeatureCallback: SupervisorDashboardFeatureCallback
) {
    private val userId: Long = supervisorDashboardFeatureArgs.userId
    private val userRoleType: UserRole.Type = supervisorDashboardFeatureArgs.userRoleType

    fun openSalesDynamicsScreen() =
        router.newRootScreen(Screens.SalesDynamicsScreen)

    fun openReceiptCheckScreen() =
        supervisorDashboardFeatureCallback.onOpenReceiptCheck(userId, userRoleType)

    fun openBranchListScreen() =
        supervisorDashboardFeatureCallback.onOpenBranchList(userId, userRoleType)

    fun openCategoryType() =
        supervisorDashboardFeatureCallback.onOpenCategoryType(userId, userRoleType)

    fun openUserListScreen() =
        supervisorDashboardFeatureCallback.onOpenUserList(userId, userRoleType)

    fun openUserSettingsScreen() =
        supervisorDashboardFeatureCallback.onOpenUserSettings(userId, userRoleType)

    fun backToRootScreen() =
        supervisorDashboardFeatureCallback.onBackFromSupervisorDashboard()

    private object Screens {

        object SalesDynamicsScreen : SupportAppScreen() {
            override fun getFragment(): Fragment? =
                SalesDynamicsFragment.newInstance()
        }
    }
}