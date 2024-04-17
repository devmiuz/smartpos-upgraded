package uz.uzkassa.smartpos.core.data.source.resource.shift.mapper

import uz.uzkassa.smartpos.core.data.source.resource.shift.model.Shift
import uz.uzkassa.smartpos.core.data.source.resource.shift.model.ShiftEntity
import uz.uzkassa.smartpos.core.utils.enums.valueOrDefault

fun List<ShiftEntity>.map() =
    map { it.map() }

fun ShiftEntity.map() =
    Shift(
        id = id,
        userId = userId,
        startDate = startDate,
        endDate = endDate,
        fiscalNumber = shift_fiscal_number,
        number = number,
        status = Shift.Status.valueOrDefault(status)
    )