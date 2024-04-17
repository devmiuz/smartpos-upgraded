package uz.uzkassa.smartpos.core.presentation.widget.textinputlayout

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import uz.uzkassa.smartpos.core.presentation.R
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class EditTextInputLayoutFormat @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {
    private val currencyTextWatcher = CurrencyTextWatcher(this, prefix)
    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            addTextChangedListener(currencyTextWatcher)
        } else {
            removeTextChangedListener(currencyTextWatcher)
        }
        handleCaseCurrencyEmpty(focused)
    }

    private fun handleCaseCurrencyEmpty(focused: Boolean) {
        if (focused) {
            if (text.toString().isEmpty()) {
                setText(prefix)
            }
        } else {
            if (text.toString() == prefix) {
                setText("")
            }
        }
    }

    private class CurrencyTextWatcher internal constructor(
        private val editText: EditText,
        private val prefix: String
    ) : TextWatcher {
        private var previousCleanString: String? = null
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable) {
            val str = editable.toString()
            if (str.length < prefix.length) {
                editText.setSelection(0)
                return
            }
            if (str == prefix) {
                return
            }
            val cleanString = str.replace(prefix, "").replace("[,]".toRegex(), "")
            if (cleanString == previousCleanString || cleanString.isEmpty()) {
                return
            }
            previousCleanString = cleanString
            val formattedString: String
            formattedString = if (cleanString.contains(".")) {
                formatDecimal(cleanString.trim { it <= ' ' })
            } else {
                formatInteger(cleanString.trim { it <= ' ' })
            }
            editText.removeTextChangedListener(this) // Remove listener
            editText.setText(formattedString)
            handleSelection()
            editText.addTextChangedListener(this) // Add back the listener
        }

        private fun formatInteger(str: String): String {
            val parsed = BigDecimal(str.replace(" ", ""))
            val symbols1 = DecimalFormatSymbols()
            symbols1.decimalSeparator = '.'
            symbols1.groupingSeparator = ' '
            val formatter = DecimalFormat("$prefix#,###", symbols1)
            return formatter.format(parsed)
        }

        private fun formatDecimal(str: String): String {
            if (str == ".") {
                return "$prefix."
            }
            val parsed = BigDecimal(str.replace(" ", ""))
            val symbols = DecimalFormatSymbols()
            symbols.decimalSeparator = '.'
            symbols.groupingSeparator = ' '
            val formatter = DecimalFormat(
                prefix + "#,###." + getDecimalPattern(str),
                symbols
            )
            formatter.roundingMode = RoundingMode.DOWN
            return formatter.format(parsed)
        }

        private fun getDecimalPattern(str: String): String {
            val decimalCount = str.length - str.indexOf(".") - 1
            val decimalPattern = StringBuilder()
            var i = 0
            while (i < decimalCount && i < MAX_DECIMAL) {
                decimalPattern.append("0")
                i++
            }
            return decimalPattern.toString()
        }

        private fun handleSelection() {
            if (editText.text.length <= MAX_LENGTH) {
                editText.setSelection(editText.text.length)
            } else {
                editText.setSelection(MAX_LENGTH)
            }
        }

    }

    companion object {
        private const val MAX_LENGTH = 20
        private const val MAX_DECIMAL = 2
        private const val prefix = ""
    }

    init {
        this.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        this.hint = prefix
        this.filters = arrayOf<InputFilter>(LengthFilter(MAX_LENGTH))
    }
}