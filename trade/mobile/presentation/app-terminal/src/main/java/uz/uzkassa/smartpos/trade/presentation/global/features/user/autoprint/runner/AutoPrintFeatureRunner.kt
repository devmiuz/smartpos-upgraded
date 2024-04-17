package uz.uzkassa.smartpos.trade.presentation.global.features.user.autoprint.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface AutoPrintFeatureRunner : FeatureRunner {

    fun run(branchId:Long,userId: Long, userRoleType: UserRole.Type, action: (Screen) -> Unit)

//    fun openCashierAuth(
//        action: (userId: Long, userRoleType: UserRole.Type) -> Unit
//    ): UserAuthFeatureRunner
//
//    fun openSupervisorAuth(
//        action: (userId: Long, userRoleType: UserRole.Type) -> Unit
//    ): UserAuthFeatureRunner

    fun back(action: () -> Unit): AutoPrintFeatureRunner
}