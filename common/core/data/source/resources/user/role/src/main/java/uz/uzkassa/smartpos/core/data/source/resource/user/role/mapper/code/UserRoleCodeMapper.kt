package uz.uzkassa.smartpos.core.data.source.resource.user.role.mapper.code

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

fun List<UserRole.Code>.lastByPriority(): UserRole.Code {
    return find { it == UserRole.Code.ROLE_AUDITOR } ?: find { it == UserRole.Code.ROLE_INTERN }
    ?: find { it == UserRole.Code.ROLE_CASHIER }
    ?: find { it == UserRole.Code.ROLE_WAREHOUSE_MANAGER }
    ?: find { it == UserRole.Code.ROLE_BRANCH_ADMIN } ?: find { it == UserRole.Code.ROLE_OWNER }
    ?: UserRole.Code.UNKNOWN
}

fun List<UserRole.Code>.firstByPriority(): UserRole.Code {
    return find { it == UserRole.Code.ROLE_OWNER } ?: find { it == UserRole.Code.ROLE_BRANCH_ADMIN }
    ?: find { it == UserRole.Code.ROLE_WAREHOUSE_MANAGER }
    ?: find { it == UserRole.Code.ROLE_CASHIER } ?: find { it == UserRole.Code.ROLE_INTERN }
    ?: find { it == UserRole.Code.ROLE_AUDITOR } ?: UserRole.Code.UNKNOWN
}

fun UserRole.Code.mapToType() = when (this) {
    UserRole.Code.ROLE_AUDITOR -> UserRole.Type.AUDITOR
    UserRole.Code.ROLE_BRANCH_ADMIN -> UserRole.Type.BRANCH_ADMIN
    UserRole.Code.ROLE_OWNER -> UserRole.Type.OWNER
    UserRole.Code.ROLE_CASHIER -> UserRole.Type.CASHIER
    UserRole.Code.ROLE_INTERN -> UserRole.Type.INTERN
    UserRole.Code.ROLE_WAREHOUSE_MANAGER -> UserRole.Type.WAREHOUSE_MANAGER
    UserRole.Code.UNKNOWN -> UserRole.Type.UNKNOWN
}