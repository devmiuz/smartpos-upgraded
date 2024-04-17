package uz.uzkassa.smartpos.core.data.source.fiscal.model.shift

import java.util.*

data class FiscalOpenShiftResult(
    val openDate: Date?,
    val samSerialNumber: String,
    val shiftNumber: Int
)