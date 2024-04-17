package uz.uzkassa.smartpos.core.utils.primitives

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

fun Number.formatToAmount(): String {
    return try {
        val charsAfterDot = toString().let { string ->
            string.lastIndexOf(".")
                .let { if (it > -1) ((string.length - 1) - (it - 1) - 1) else 0 }
        }

        var suffix = ""
        repeat(charsAfterDot) { suffix += "0" }

        val formatted: String = String.format("%,.${charsAfterDot}f", this)
        val endsWithComma = ",$suffix"
        val endsWithDot = ".$suffix"
        val endsWithDecimal = with(formatted) { endsWith(endsWithComma) || endsWith(endsWithDot) }
        if (endsWithDecimal) {
            formatted.substring(0, formatted.length - (1 + charsAfterDot))
                .toDouble()
                .roundToString()
        }
        else formatted.toDouble().roundToString()
    } catch (e: Exception) {
        toDouble().roundToString()
    }
}

@Suppress("HasPlatformType")
fun Number.roundToString(pattern: String = "#.###"): String {
    return DecimalFormat(pattern, DecimalFormatSymbols(Locale.ROOT)).format(this)
}

fun Number.addWhitespace(): String {
    val formatString = "###,###.######"
    val formatSymbols = DecimalFormatSymbols(Locale.ENGLISH)
    formatSymbols.decimalSeparator = '.'
    formatSymbols.groupingSeparator = ' '
    val formatter = DecimalFormat(formatString, formatSymbols)
    return formatter.format(this)
}