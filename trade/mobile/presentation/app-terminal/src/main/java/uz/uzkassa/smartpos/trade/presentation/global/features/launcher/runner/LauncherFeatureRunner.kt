package uz.uzkassa.smartpos.trade.presentation.global.features.launcher.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

interface LauncherFeatureRunner {

    fun run(action: (Screen) -> Unit)

    fun openCashierAuth(
        action: (branchId: Long, userId: Long, userRoleType: UserRole.Type) -> Unit
    ): LauncherFeatureRunner

    fun openSupervisorAuth(
        action: (branchId: Long, userId: Long, userRoleType: UserRole.Type) -> Unit
    ): LauncherFeatureRunner
}