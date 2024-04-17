package uz.uzkassa.smartpos.core.data.source.resource.shift.model

import uz.uzkassa.smartpos.core.utils.enums.EnumCompanion
import java.util.*

data class Shift(
    val id: Long,
    val userId: Long,
    val startDate: Date,
    val endDate: Date?,
    val fiscalNumber: Long?,
    val number: Long,
    val status: Status
) {

    enum class Status {
        OPEN, CLOSED;

        companion object : EnumCompanion<Status> {
            override val DEFAULT: Status = CLOSED
        }
    }
}