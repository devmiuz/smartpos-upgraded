package uz.uzkassa.smartpos.feature.user.auth.data.model

import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

internal data class UserOwner(
    val user: User,
    val actualFeatureByUserRoleType: UserRole.Type
)