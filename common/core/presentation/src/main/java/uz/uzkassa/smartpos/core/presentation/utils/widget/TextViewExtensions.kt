package uz.uzkassa.smartpos.core.presentation.utils.widget

import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import uz.uzkassa.smartpos.core.presentation.utils.content.getAmountSpan
import uz.uzkassa.smartpos.core.presentation.utils.content.getTextWithPostfixSpan
import java.math.BigDecimal

fun TextView.addFilters(vararg filterArray: InputFilter) {
    filters = filters.toMutableList().apply { addAll(filterArray) }.toTypedArray()
}

fun TextView.text(): String = text?.toString() ?: ""

fun TextView.setSpannableText(spannableStringBuilder: SpannableStringBuilder) =
    setText(spannableStringBuilder, TextView.BufferType.SPANNABLE)

fun TextView.setTextAmount(
    bigDecimal: BigDecimal, @StringRes currencyStringResId: Int, @ColorRes textColor: Int
) = setSpannableText(
    context.getAmountSpan(
        currencyStringResId = currencyStringResId,
        bigDecimal = bigDecimal,
        amountColor = ContextCompat.getColor(context, textColor),
        currencyColor = ContextCompat.getColor(context, textColor)
    )
)

fun TextView.setTextAmount(
    bigDecimal: BigDecimal, @StringRes currencyStringResId: Int,
    @ColorRes amountColorResId: Int? = null, @ColorRes currencyColorResId: Int? = null
) = setSpannableText(
    context.getAmountSpan(
        currencyStringResId = currencyStringResId,
        bigDecimal = bigDecimal,
        amountColor = amountColorResId?.let { ContextCompat.getColor(context, it) }
            ?: currentTextColor,
        currencyColor = currencyColorResId?.let { ContextCompat.getColor(context, it) }
            ?: currentTextColor
    )
)

@Suppress("DEPRECATION")
fun TextView.setTextAppearanceCompat(@StyleRes resId: Int) =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) setTextAppearance(context, resId)
    else setTextAppearance(resId)

fun TextView.setTextWithPostfix(
    text: String, postfix: String,
    @ColorRes textColorResId: Int? = null, @ColorRes postfixColorResId: Int? = null
) = setSpannableText(
    SpannableStringBuilder().getTextWithPostfixSpan(
        text = text,
        postfix = postfix,
        textColor = textColorResId?.let { ContextCompat.getColor(context, it) } ?: currentTextColor,
        postfixColor = postfixColorResId?.let { ContextCompat.getColor(context, it) }
            ?: currentTextColor
    )
)

inline fun TextView.setTextChangedListener(
    owner: LifecycleOwner,
    crossinline beforeTextChanged: (
        text: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) -> Unit = { _, _, _, _ -> },
    crossinline onTextChanged: (
        text: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) -> Unit = { _, _, _, _ -> },
    crossinline afterTextChanged: (text: Editable?) -> Unit = {}
) {
    val textWatcher = addTextChangedListener(beforeTextChanged, onTextChanged, afterTextChanged)
    val observer: LifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            removeTextChangedListener(textWatcher)
            owner.lifecycle.removeObserver(this)
        }
    }
    owner.lifecycle.addObserver(observer)
}

fun TextView.setTextWithPostfix(
    @StringRes stingResId: Int, postfix: String,
    @ColorRes textColorResId: Int? = null, @ColorRes postfixColorResId: Int? = null
) = setSpannableText(
    SpannableStringBuilder().getTextWithPostfixSpan(
        text = context.getString(stingResId),
        postfix = postfix,
        textColor = textColorResId?.let { ContextCompat.getColor(context, it) } ?: currentTextColor,
        postfixColor = postfixColorResId?.let { ContextCompat.getColor(context, it) }
            ?: currentTextColor
    )
)