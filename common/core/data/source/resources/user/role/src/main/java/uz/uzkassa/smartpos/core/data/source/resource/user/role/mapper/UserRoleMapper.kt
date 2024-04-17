package uz.uzkassa.smartpos.core.data.source.resource.user.role.mapper

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRoleResponse

fun UserRole.mapToResponse() =
    UserRoleResponse(
        code = code.name,
        type = type.name,
        priority = priority,
        nameRu = nameRu
    )