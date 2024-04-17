package uz.uzkassa.smartpos.core.data.source.resource.user.role.mapper

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRoleEntity

fun List<UserRoleEntity>.map() =
    map { it.map() }

fun UserRoleEntity.map() =
    UserRole(
        code = code,
        type = type,
        priority = priority,
        nameRu = nameRu
    )