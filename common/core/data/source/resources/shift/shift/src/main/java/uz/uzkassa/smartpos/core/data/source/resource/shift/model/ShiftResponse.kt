package uz.uzkassa.smartpos.core.data.source.resource.shift.model

import android.annotation.SuppressLint
import kotlinx.serialization.Decoder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.DateSerializer
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class ShiftResponse(
    @SerialName("id")
    val id: Long,

    @Serializable(with = ShiftDateSerializer::class)
    @SerialName("startDateTime")
    val startDate: Date,

    @Serializable(with = ShiftDateSerializer::class)
    @SerialName("endDateTime")
    val endDate: Date? = null,

    @SerialName("fiscalNumber")
    val fiscalNumber: Long?,

    @SerialName("number")
    val number: Long,

    @SerialName("status")
    val status: Status
) {

    @Suppress("unused")
    enum class Status {
        OPEN,
        CLOSED
    }

    @Suppress("SpellCheckingInspection")
    private object ShiftDateSerializer :
        DateSerializer.NotNullable("yyyy-MM-dd'T'HH:mm:ss.SSSSS") {

        @SuppressLint("ConstantLocale")
        private val dateFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        override fun deserialize(decoder: Decoder): Date {
            return runCatching { super.deserialize(decoder) }.getOrNull() ?:
                runCatching { dateFormat.parse(decoder.decodeString()) }.getOrNull() ?: Date()
        }
    }
}