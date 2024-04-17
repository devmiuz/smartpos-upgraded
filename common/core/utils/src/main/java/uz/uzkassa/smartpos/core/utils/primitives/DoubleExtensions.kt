package uz.uzkassa.smartpos.core.utils.primitives

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

private val decimalFormat: DecimalFormat =
    DecimalFormat("#.###", DecimalFormatSymbols(Locale.ROOT))

operator fun Double.div(bigDecimal: BigDecimal): Double =
    decimalFormat.format(div(bigDecimal.toDouble())).toDouble()

fun Double.round(pattern: String = "#.###"): Double =
    roundToString(pattern).toDouble()

fun Double.roundToBigDecimal(pattern: String = "#.##") =
    BigDecimal(roundToString(pattern))

fun Double.roundToString(): String =
    StringBuilder(roundToString("#.########")).round().toString()

@OptIn(ExperimentalStdlibApi::class)
private fun StringBuilder.round(): StringBuilder {
    if (contains(".") && endsWith("0")) deleteAt(length)
    else return this
    return round()
}