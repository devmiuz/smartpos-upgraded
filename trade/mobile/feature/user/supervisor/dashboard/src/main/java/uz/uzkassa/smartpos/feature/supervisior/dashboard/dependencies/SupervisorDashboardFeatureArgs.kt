package uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

interface SupervisorDashboardFeatureArgs {

    val branchId: Long

    val userId: Long

    val userRoleType: UserRole.Type
}