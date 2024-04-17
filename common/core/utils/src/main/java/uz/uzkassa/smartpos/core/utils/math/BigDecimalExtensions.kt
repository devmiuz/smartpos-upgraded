package uz.uzkassa.smartpos.core.utils.math

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

private val decimalFormat: DecimalFormat =
    DecimalFormat("#.##", DecimalFormatSymbols(Locale.US))

fun BigDecimal.round(): BigDecimal =
    BigDecimal(decimalFormat.format(this))

fun BigDecimal.multiply(double: Double): BigDecimal =
    multiply(BigDecimal(double)).setScale(7, RoundingMode.HALF_UP)

fun BigDecimal.multiply(int: Int): BigDecimal =
    multiply(BigDecimal(int)).round()

operator fun BigDecimal.div(double: Double): Double =
    toDouble() / double

operator fun BigDecimal.times(double: Double): Double =
    toDouble() * double

operator fun BigDecimal.times(int: Int): BigDecimal =
    multiply(int)

fun Collection<BigDecimal>.sum(): BigDecimal {
    var bigDecimal: BigDecimal = BigDecimal.ZERO
    onEach { bigDecimal += it }
    return bigDecimal.setScale(2, RoundingMode.DOWN)
}

fun BigDecimal.toStringCompat(): String {
    val charsCountAfterDot = toString().let { string ->
        string.lastIndexOf(".").let { if (it > -1) ((string.length - 1) - (it - 1) - 1) else 0 }
    }

    var suffix = ""

    repeat(charsCountAfterDot) { suffix += "0" }

    val formattedString: String = String.format("%,.${charsCountAfterDot}f", this)
    val probablyStringEndingWithComma = ",$suffix"
    val probablyStringEndingWithDot = ".$suffix"

    val isEndsWithProbablyEndings: Boolean =
        formattedString.endsWith(probablyStringEndingWithComma) ||
                formattedString.endsWith(probablyStringEndingWithDot)

    return if (isEndsWithProbablyEndings)
        formattedString.substring(0, formattedString.length - (1 + charsCountAfterDot))
    else formattedString
}