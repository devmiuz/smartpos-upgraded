package uz.uzkassa.smartpos.feature.user.saving.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

interface UserSavingFeatureArgs {

    val branchId: Long

    val userId: Long?

    val userRoleType: UserRole.Type
}