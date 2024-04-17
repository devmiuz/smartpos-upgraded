package uz.uzkassa.smartpos.core.utils.kserialization.serializer

import kotlinx.serialization.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateSerializer {

    open class NotNullable(pattern: String = "yyyy-MM-dd") : KSerializer<Date> {
        private val dateFormat: DateFormat =
            SimpleDateFormat(pattern, Locale.getDefault())

        override val descriptor = PrimitiveDescriptor(
            serialName = "NotNullableDateSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun deserialize(decoder: Decoder): Date =
            checkNotNull(dateFormat.parse(decoder.decodeString()))

        override fun serialize(encoder: Encoder, value: Date) {
            encoder.encodeString(dateFormat.format(value))
        }
    }

    open class Nullable(pattern: String = "yyyy-MM-dd") : KSerializer<Date?> {
        private val dateFormat: DateFormat =
            SimpleDateFormat(pattern, Locale.getDefault())

        override val descriptor = PrimitiveDescriptor(
            serialName = "NullableDateSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun deserialize(decoder: Decoder): Date? =
            dateFormat.parse(decoder.decodeString())

        override fun serialize(encoder: Encoder, value: Date?) {
            value?.let { encoder.encodeString(dateFormat.format(it)) } ?: encoder.encodeNull()
        }
    }
}