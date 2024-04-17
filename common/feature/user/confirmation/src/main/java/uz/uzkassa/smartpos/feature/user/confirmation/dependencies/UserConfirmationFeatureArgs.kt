package uz.uzkassa.smartpos.feature.user.confirmation.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

interface UserConfirmationFeatureArgs {

    val branchId: Long

    val userRoleType: UserRole.Type
}