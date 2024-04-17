package uz.uzkassa.smartpos.core.data.source.resource.account.mapper

import uz.uzkassa.smartpos.core.data.source.resource.account.model.Account
import uz.uzkassa.smartpos.core.data.source.resource.account.model.AccountResponse
import uz.uzkassa.smartpos.core.data.source.resource.user.role.mapper.code.mapToType
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.utils.enums.valueOrDefault

fun AccountResponse.map() =
    Account(
        id = id,
        phoneNumber = phoneNumber,
        isActivated = isActivated,
        userRoleTypes = userRoleCodes.map { UserRole.Code.valueOrDefault(it).mapToType() }
    )