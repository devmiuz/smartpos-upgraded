package uz.uzkassa.smartpos.core.data.source.resource.user.mapper

import uz.uzkassa.smartpos.core.data.source.resource.fullname.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.data.source.resource.language.model.LanguageEntity
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserEntity
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserResponse

fun List<UserResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun UserResponse.mapToEntity() =
    UserEntity(
        id = id,
        branchId = branchId,
        phoneNumber = phoneNumber,
        language = LanguageEntity(languageKey = languageKey ?: Language.RUSSIAN.name),
        isActivated = isActivated,
        isBlocked = isBlocked ?: false,
        isOwner = isOwner,
        fullName = fullName.mapToEntity(),
        userRoleCodes = userRoleCodes.toTypedArray(),
        isDismissed = isDismissed
    )