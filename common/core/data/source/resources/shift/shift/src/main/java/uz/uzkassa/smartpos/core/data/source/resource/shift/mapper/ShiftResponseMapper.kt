package uz.uzkassa.smartpos.core.data.source.resource.shift.mapper

import uz.uzkassa.smartpos.core.data.source.resource.shift.model.ShiftEntity
import uz.uzkassa.smartpos.core.data.source.resource.shift.model.ShiftResponse

fun ShiftResponse.mapToEntity(userId: Long) =
    ShiftEntity(
        id = id,
        startDate = startDate,
        endDate = endDate,
        shift_fiscal_number = fiscalNumber,
        number = number,
        status = status.name,
        userId = userId
    )