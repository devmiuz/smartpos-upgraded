package uz.uzkassa.smartpos.trade.presentation.global.coordinators

import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.sale.runner.CashierSaleFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.launcher.runner.LauncherFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.supervisor.dashboard.runner.SupervisorDashboardFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter
import javax.inject.Inject

class FeaturesCoordinator @Inject constructor(
    private val cashierSaleFeatureRunner: CashierSaleFeatureRunner,
    private val globalRouter: GlobalRouter,
    private val launcherFeatureRunner: LauncherFeatureRunner,
    private val supervisorDashboardFeatureRunner: SupervisorDashboardFeatureRunner
) {

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    fun launch() {
        launcherFeatureRunner
            .openCashierAuth { branchId, userId, userRoleType ->
                cashierSaleFeatureRunner
                    .back { globalRouter.exit() }
                    .run(branchId, userId, userRoleType) { globalRouter.replaceScreen(it) }
            }
            .openSupervisorAuth { branchId, userId, userRoleType ->
                supervisorDashboardFeatureRunner
                    .back { globalRouter.exit() }
                    .run(branchId, userId, userRoleType) { globalRouter.replaceScreen(it) }
            }
            .run { globalRouter.newRootScreen(it) }
    }
}