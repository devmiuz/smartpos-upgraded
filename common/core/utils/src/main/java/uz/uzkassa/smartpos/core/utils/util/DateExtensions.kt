package uz.uzkassa.smartpos.core.utils.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.withoutTime(): Date =
    Calendar.getInstance().apply {
        time = this@withoutTime
        set(Calendar.HOUR_OF_DAY, 0)
        clear(Calendar.MINUTE)
        clear(Calendar.SECOND)
        clear(Calendar.MILLISECOND)
    }.time

fun Date.toString(format: String): String =
    SimpleDateFormat(format, Locale.getDefault()).format(this)

fun Date.formatOFDDateString(): String =
    SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(this)