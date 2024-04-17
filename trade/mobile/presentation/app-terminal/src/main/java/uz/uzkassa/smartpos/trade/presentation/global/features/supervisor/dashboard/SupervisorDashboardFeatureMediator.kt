package uz.uzkassa.smartpos.trade.presentation.global.features.supervisor.dashboard

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies.SupervisorDashboardFeatureArgs
import uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies.SupervisorDashboardFeatureCallback
import uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.SupervisorDashboardFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.list.runner.BranchListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.category.type.runner.CategoryTypeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.receipt_check.runner.ReceiptCheckFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.supervisor.dashboard.runner.SupervisorDashboardFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.list.runner.UserListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.main.runner.UserSettingsFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class SupervisorDashboardFeatureMediator(
    private val receiptCheckFeatureRunner: ReceiptCheckFeatureRunner,
    private val branchListFeatureRunner: BranchListFeatureRunner,
    private val categoryTypeFeatureRunner: CategoryTypeFeatureRunner,
    private val router: Router,
    private val userListFeatureRunner: UserListFeatureRunner,
    private val userSettingsFeatureRunner: UserSettingsFeatureRunner
) : FeatureMediator, SupervisorDashboardFeatureArgs, SupervisorDashboardFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    override var branchId: Long by Delegates.notNull()
    override var userId: Long by Delegates.notNull()
    override var userRoleType: UserRole.Type by Delegates.notNull()

    val featureRunner: SupervisorDashboardFeatureRunner =
        FeatureRunnerImpl()

    override fun onOpenBranchList(userId: Long, userRoleType: UserRole.Type) {
        branchListFeatureRunner
            .back { router.backTo(Screens.SupervisorDashboard) }
            .run(branchId, this.userRoleType) { router.navigateTo(it) }
    }

    override fun onOpenReceiptCheck(userId: Long, userRoleType: UserRole.Type) {
        receiptCheckFeatureRunner
            .back { router.backTo(Screens.SupervisorDashboard) }
            .run(branchId, this.userRoleType) { router.navigateTo(it) }
    }

    override fun onOpenCategoryType(userId: Long, userRoleType: UserRole.Type) {
        categoryTypeFeatureRunner
            .back { router.backTo(Screens.SupervisorDashboard) }
            .run(branchId) { router.navigateTo(it) }
    }

    override fun onOpenUserList(userId: Long, userRoleType: UserRole.Type) {
        userListFeatureRunner
            .back { router.backTo(Screens.SupervisorDashboard) }
            .run(branchId, this.userRoleType) { router.navigateTo(it) }
    }

    override fun onOpenUserSettings(userId: Long, userRoleType: UserRole.Type) {
        userSettingsFeatureRunner
            .back { router.backTo(Screens.SupervisorDashboard) }
            .run(userId, userRoleType) { router.navigateTo(it) }
    }

    override fun onBackFromSupervisorDashboard() =
        backAction.invoke()

    private inner class FeatureRunnerImpl : SupervisorDashboardFeatureRunner {

        override fun run(
            branchId: Long,
            userId: Long,
            userRoleType: UserRole.Type,
            action: (Screen) -> Unit
        ) {
            this@SupervisorDashboardFeatureMediator.branchId = branchId
            this@SupervisorDashboardFeatureMediator.userId = userId
            this@SupervisorDashboardFeatureMediator.userRoleType = userRoleType
            action.invoke(Screens.SupervisorDashboard)
        }

        override fun back(action: () -> Unit): SupervisorDashboardFeatureRunner {
            backAction = action
            return this
        }
    }

    private object Screens {

        object SupervisorDashboard : SupportAppScreen() {
            override fun getFragment(): Fragment =
                SupervisorDashboardFragment.newInstance()
        }
    }
}