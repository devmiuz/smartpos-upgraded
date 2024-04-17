package uz.uzkassa.smartpos.core.data.source.resource.account.model

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

open class Account(
    val id: Long,
    val phoneNumber: String,
    val isActivated: Boolean,
    val userRoleTypes: List<UserRole.Type>
)