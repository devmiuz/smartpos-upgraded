package uz.uzkassa.smartpos.core.presentation.utils.inputmask

import android.widget.EditText
import com.redmadrobot.inputmask.MaskedTextChangedListener

class InputMask(
    maskFormat: MaskFormat,
    editText: EditText,
    text: String? = null,
    onTextChanged: (value: String) -> Unit
) {
    private val listener: MaskedTextChangedListener =
        MaskedTextChangedListener.installOn(
            editText = editText,
            primaryFormat = maskFormat.format,
            valueListener = ValueListener(onTextChanged)
        )

    init {
        listener.setText(text ?: "")
    }

    private class ValueListener(
        private val onTextChanged: (value: String) -> Unit
    ) : MaskedTextChangedListener.ValueListener {
        override fun onTextChanged(
            maskFilled: Boolean,
            extractedValue: String,
            formattedValue: String
        ) = onTextChanged.invoke(formattedValue)
    }

    @Suppress("ClassName")
    object SIX_DIGITS_FORMAT : MaskFormat {
        override val format: String = "[00] [00] [00]"
    }

    @Suppress("ClassName")
    object UZB_PHONE_FORMAT : MaskFormat {
        override val format: String = "+998 ([00]) [000] [00] [00]"
    }

    @Suppress("ClassName")
    object CARD_NUMBER_FORMAT : MaskFormat {
        override val format: String = "[0000] [0000] [0000] [0000]"
    }

    @Suppress("ClassName")
    object CARD_EXPIRY_DATE_FORMAT : MaskFormat {
        override val format: String = "[00]{/}[00]"
    }
}