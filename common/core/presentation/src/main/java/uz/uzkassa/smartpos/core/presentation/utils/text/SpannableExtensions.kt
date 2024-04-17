package uz.uzkassa.smartpos.core.presentation.utils.text

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan

fun SpannableString.setStyleSpan(
    typeface: Int,
    start: Int = 0,
    end: Int = length
): SpannableString =
    apply { setSpan(StyleSpan(typeface), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

fun SpannableString.setColorSpan(color: Int, start: Int = 0, end: Int = length): SpannableString =
    apply { setSpan(ForegroundColorSpan(color), start, end, 0) }

fun SpannableString.setSizeSpan(
    proportion: Float,
    start: Int = 0,
    end: Int = length
): SpannableString =
    apply { setSpan(RelativeSizeSpan(proportion), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

@Suppress("SpellCheckingInspection")
fun Spannable.formatArgs(vararg arrayOfSpannables: Spannable): Spannable {
    val formatIndexes: List<Pair<Int, String>> =
        arrayListOf<Pair<Int, String>>()
            .also { list ->
                toString().split(" ").forEach { s ->
                    if (s.matches("%[0-9][$][d.s]$".toRegex()))
                        list.add(Pair(toString().indexOf(s), s))
                }
            }

    val spannableStringBuilder = SpannableStringBuilder(this)
    val spannableArguments: MutableList<Spannable> = arrayOfSpannables.toMutableList()

    var previousSpannableLength = 0

    formatIndexes.forEachIndexed { index, pair ->
        val formatIndex: Int = pair.first
        val formatArgument: String = pair.second
        val spannable: Spannable = spannableArguments[index].also { spannableArguments.remove(it) }

        val startIndex: Int = previousSpannableLength + formatIndex
        val endIndex: Int = startIndex + formatArgument.length

        spannableStringBuilder.replace(startIndex, endIndex, spannable)
        previousSpannableLength = spannable.length
    }

    return spannableStringBuilder
}