package uz.uzkassa.smartpos.trade.companion.data.database.impl.typeconverter

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.LongArraySerializer
import kotlinx.serialization.json.*
import java.math.BigDecimal
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Suppress("EXPERIMENTAL_API_USAGE")
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

    @JvmStatic
    @TypeConverter
    fun fromLongArray(array: LongArray?): String? = array?.let {
        Json.stringify(LongArraySerializer(), it)
    }

    @JvmStatic
    @TypeConverter
    fun toLongArray(string: String?): LongArray? = string?.let {
        Json.parse(LongArraySerializer(), it).toTypedArray().toLongArray()
    }

    @JvmStatic
    @TypeConverter
    fun fromStringArray(array: Array<String>?): String? = array?.let { it ->
        Json.stringify(JsonArraySerializer, JsonArray(it.map { JsonPrimitive(it) }))
    }

    @JvmStatic
    @TypeConverter
    fun toStringArray(string: String?): Array<String>? = string?.let { it ->
        Json.parse(JsonArraySerializer, it).map { it.content }.toTypedArray()
    }
}