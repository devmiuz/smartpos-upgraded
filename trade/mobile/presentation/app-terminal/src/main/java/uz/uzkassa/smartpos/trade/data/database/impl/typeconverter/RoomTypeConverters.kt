package uz.uzkassa.smartpos.trade.data.database.impl.typeconverter

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.LongArraySerializer
import kotlinx.serialization.json.*
import java.math.BigDecimal
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object RoomTypeConverters {
    private val dateFormat: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale("uz", "UZ"))

    @JvmStatic
    @TypeConverter
    fun fromBigDecimal(bigDecimal: BigDecimal?): String? =
        bigDecimal?.toString()

    @JvmStatic
    @TypeConverter
    fun toBigDecimal(string: String?): BigDecimal? =
        string?.let { BigDecimal(it) }

    @JvmStatic
    @TypeConverter
    fun fromDate(date: Date?): String? =
        date?.time?.let { dateFormat.format(it) }

    @JvmStatic
    @TypeConverter
    fun toDate(string: String?): Date? =
        string?.let { dateFormat.parse(it) }

    @Suppress("EXPERIMENTAL_API_USAGE")
    @JvmStatic
    @TypeConverter
    fun fromLongArray(array: LongArray?): String? = array?.let {
        Json.stringify(LongArraySerializer(), it)
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    @JvmStatic
    @TypeConverter
    fun toLongArray(string: String?): LongArray? = string?.let {
        Json.parse(LongArraySerializer(), it).toTypedArray().toLongArray()
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    @JvmStatic
    @TypeConverter
    fun fromStringArray(array: Array<String>?): String? = array?.let { it ->
        runCatching { Json.stringify(JsonArraySerializer, JsonArray(it.map { JsonPrimitive(it) })) }
            .getOrNull()
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    @JvmStatic
    @TypeConverter
    fun toStringArray(string: String?): Array<String>? = string?.let { it ->
        runCatching { Json.parse(JsonArraySerializer, it).map { it.content } }
            .getOrNull()
            ?.toTypedArray()
    }
}