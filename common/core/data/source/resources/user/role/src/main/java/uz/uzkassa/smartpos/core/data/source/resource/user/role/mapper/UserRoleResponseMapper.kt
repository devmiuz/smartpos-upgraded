package uz.uzkassa.smartpos.core.data.source.resource.user.role.mapper

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRoleEntity
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRoleResponse

fun List<UserRoleResponse>.map() =
    map { it.map() }

fun List<UserRoleResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun UserRoleResponse.map() =
    UserRole(
        code = code,
        type = type,
        priority = priority,
        nameRu = nameRu
    )

fun UserRoleResponse.mapToEntity() =
    UserRoleEntity(
        code = code,
        type = type,
        priority = priority,
        nameRu = nameRu
    )