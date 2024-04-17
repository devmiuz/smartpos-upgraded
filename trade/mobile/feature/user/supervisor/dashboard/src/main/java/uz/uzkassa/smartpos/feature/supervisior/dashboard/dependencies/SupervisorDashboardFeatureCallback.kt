package uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

interface SupervisorDashboardFeatureCallback {

    fun onOpenBranchList(userId: Long, userRoleType: UserRole.Type)

    fun onOpenReceiptCheck(userId: Long, userRoleType: UserRole.Type)

    fun onOpenCategoryType(userId: Long, userRoleType: UserRole.Type)

    fun onOpenUserList(userId: Long, userRoleType: UserRole.Type)

    fun onOpenUserSettings(userId: Long, userRoleType: UserRole.Type)

    fun onBackFromSupervisorDashboard()
}