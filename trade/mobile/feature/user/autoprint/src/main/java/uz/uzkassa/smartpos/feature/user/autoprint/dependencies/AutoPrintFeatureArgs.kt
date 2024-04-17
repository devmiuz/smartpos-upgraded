package uz.uzkassa.smartpos.feature.user.autoprint.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

interface AutoPrintFeatureArgs {

    val branchId: Long

    val userId: Long

    val userRoleType: UserRole.Type
}