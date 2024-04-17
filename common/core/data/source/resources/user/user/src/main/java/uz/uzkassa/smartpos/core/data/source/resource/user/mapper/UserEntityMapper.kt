package uz.uzkassa.smartpos.core.data.source.resource.user.mapper

import uz.uzkassa.smartpos.core.data.source.resource.fullname.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.language.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserRelation
import uz.uzkassa.smartpos.core.data.source.resource.user.role.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

fun List<UserRelation>.map() =
    map { it.map() }

fun UserRelation.map(): User =
    User(
        id = userEntity.id,
        branchId = userEntity.branchId,
        phoneNumber = userEntity.phoneNumber,
        language = userEntity.language.map(),
        isOwner = userEntity.isOwner,
        isDismissed = userEntity.isDismissed,
        isActivated = userEntity.isActivated,
        isBlocked = userEntity.isBlocked,
        fullName = userEntity.fullName.map(),
        userRole = userRoleEntities.minBy { it.priority }?.map() ?: UserRole.UNKNOWN,
        userRoles = userRoleEntities.map(),
        userRoleCodes = userEntity.userRoleCodes
    )