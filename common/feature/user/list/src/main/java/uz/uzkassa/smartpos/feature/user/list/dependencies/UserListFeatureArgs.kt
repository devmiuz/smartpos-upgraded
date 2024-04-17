package uz.uzkassa.smartpos.feature.user.list.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

interface UserListFeatureArgs {

    val branchId: Long

    val userRoleType: UserRole.Type
}