package uz.uzkassa.smartpos.core.presentation.utils.content

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.presentation.utils.text.setColorSpan
import uz.uzkassa.smartpos.core.presentation.utils.text.setSizeSpan
import uz.uzkassa.smartpos.core.utils.math.toStringCompat
import java.math.BigDecimal

val Context.colorAccent: Int
    get() = getColorByAttr(R.attr.colorAccent)

val Context.colorPrimary: Int
    get() = getColorByAttr(R.attr.colorPrimary)

val Context.colorPrimaryDark: Int
    get() = getColorByAttr(R.attr.colorPrimaryDark)

fun Context.getAmountSpan(
    @StringRes currencyStringResId: Int, bigDecimal: BigDecimal,
    @ColorInt amountColor: Int? = null, @ColorInt currencyColor: Int? = null
): SpannableStringBuilder {
    val amountString: String = bigDecimal.toStringCompat()
    val amountCurrencyString: String = getString(currencyStringResId, amountString)

    val startAmountIndex = 0
    val endAmountIndex: Int = amountString.length

    val currencyString: String = amountCurrencyString.removeRange(startAmountIndex, endAmountIndex)

    val startCurrencyIndex = 0
    val endCurrencyIndex: Int = currencyString.length

    val completeAmountColor: Int = amountColor ?: Color.BLACK

    val completeCurrencyColor: Int = currencyColor ?: Color.BLACK

    val spannableStringBuilder = SpannableStringBuilder()

    spannableStringBuilder.append(
        SpannableString(amountString)
            .setColorSpan(completeAmountColor, startAmountIndex, endAmountIndex)
            .setSizeSpan(1F, startAmountIndex, endAmountIndex)
    )

    spannableStringBuilder.append(
        SpannableString(currencyString)
            .setColorSpan(completeCurrencyColor, startCurrencyIndex, endCurrencyIndex)
            .setSizeSpan(0.8F, startCurrencyIndex, endCurrencyIndex)
    )

    return spannableStringBuilder
}

@Suppress("unused")
fun SpannableStringBuilder.getTextWithPostfixSpan(
    text: String,
    postfix: String, @ColorInt textColor: Int? = null, @ColorInt postfixColor: Int? = null
): SpannableStringBuilder =
    SpannableStringBuilder()
        .append(
            SpannableString(text)
                .setColorSpan(textColor ?: Color.BLACK, 0, text.length)
                .setSizeSpan(1F, 0, text.length)
        )
        .append(
            SpannableString(" $postfix")
                .setColorSpan(postfixColor ?: Color.BLACK, 0, postfix.length + 1)
                .setSizeSpan(0.8F, 0, postfix.length + 1)
        )


private fun Context.getColorByAttr(@AttrRes resId: Int): Int {
    val typedArray: TypedArray = obtainStyledAttributes(TypedValue().data, intArrayOf(resId))
    val color: Int = typedArray.getColor(0, 0)
    typedArray.recycle()
    return color
}