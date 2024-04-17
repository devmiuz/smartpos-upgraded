package uz.uzkassa.smartpos.core.data.source.resource.user.role.mapper.type

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

fun List<UserRole.Type>.lastByPriority(): UserRole.Type {
    return find { it == UserRole.Type.AUDITOR } ?: find { it == UserRole.Type.INTERN }
    ?: find { it == UserRole.Type.CASHIER } ?: find { it == UserRole.Type.WAREHOUSE_MANAGER }
    ?: find { it == UserRole.Type.BRANCH_ADMIN } ?: find { it == UserRole.Type.OWNER }
    ?: UserRole.Type.UNKNOWN
}

fun List<UserRole.Type>.firstByPriority(): UserRole.Type {
    return find { it == UserRole.Type.OWNER } ?: find { it == UserRole.Type.BRANCH_ADMIN }
    ?: find { it == UserRole.Type.WAREHOUSE_MANAGER } ?: find { it == UserRole.Type.CASHIER }
    ?: find { it == UserRole.Type.INTERN } ?: find { it == UserRole.Type.AUDITOR }
    ?: UserRole.Type.UNKNOWN
}

fun UserRole.Type.mapToCode() = when (this) {
    UserRole.Type.AUDITOR -> UserRole.Code.ROLE_AUDITOR
    UserRole.Type.BRANCH_ADMIN -> UserRole.Code.ROLE_BRANCH_ADMIN
    UserRole.Type.OWNER -> UserRole.Code.ROLE_OWNER
    UserRole.Type.CASHIER -> UserRole.Code.ROLE_CASHIER
    UserRole.Type.INTERN -> UserRole.Code.ROLE_INTERN
    UserRole.Type.WAREHOUSE_MANAGER -> UserRole.Code.ROLE_WAREHOUSE_MANAGER
    UserRole.Type.UNKNOWN -> UserRole.Code.UNKNOWN
}