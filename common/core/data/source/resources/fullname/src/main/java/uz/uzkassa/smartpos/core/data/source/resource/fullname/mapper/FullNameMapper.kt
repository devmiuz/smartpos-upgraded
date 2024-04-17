package uz.uzkassa.smartpos.core.data.source.resource.fullname.mapper

import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullName
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullNameEntity
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullNameResponse

fun FullNameEntity.map() =
    FullName(
        firstName = firstName,
        lastName = lastName,
        patronymic = patronymic
    )

fun FullNameResponse.map() =
    FullName(
        firstName = firstName,
        lastName = lastName,
        patronymic = patronymic
    )

fun FullNameResponse.mapToEntity() =
    FullNameEntity(
        firstName = firstName,
        lastName = lastName,
        patronymic = patronymic
    )

fun FullName.mapToResponse() =
    FullNameResponse(
        firstName = firstName,
        lastName = lastName,
        patronymic = patronymic
    )