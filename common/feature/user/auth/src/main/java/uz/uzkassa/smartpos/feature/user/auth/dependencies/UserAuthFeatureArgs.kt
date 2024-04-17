package uz.uzkassa.smartpos.feature.user.auth.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

interface UserAuthFeatureArgs {

    val userId: Long

    val userRoleType: UserRole.Type
}