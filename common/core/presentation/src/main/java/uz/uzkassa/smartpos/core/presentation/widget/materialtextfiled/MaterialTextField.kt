package uz.uzkassa.smartpos.core.presentation.widget.materialtextfiled

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import uz.uzkassa.smartpos.core.presentation.R


class MaterialTextField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val textInputLayout: TextInputLayout
    private val textInputEditText: TextInputEditText

    val editableText: Editable
        get() = textInputEditText.editableText

    var error: CharSequence?
        get() = textInputLayout.error
        set(value) {
            textInputLayout.error = value
        }

    var helperText: CharSequence?
        get() = textInputLayout.helperText
        set(value) {
            textInputLayout.helperText = value
        }

    var hint: CharSequence?
        get() = textInputLayout.hint
        set(value) {
            textInputLayout.hint = value
        }

    var text: Editable?
        get() = textInputEditText.text
        set(value) {
            textInputEditText.text = value
        }

    init {
        val view: View =
            LayoutInflater
                .from(context)
                .inflate(R.layout.core_presentation_widget_materialtextfield, this, true)

        textInputLayout = view.findViewById(android.R.id.content)
        textInputEditText = view.findViewById(android.R.id.inputExtractEditText)

        textInputLayout.id = System.currentTimeMillis().toInt()
        textInputEditText.id = System.currentTimeMillis().toInt()

        val obtainStyledAttributes: TypedArray =
            getContext().obtainStyledAttributes(attrs, R.styleable.MaterialTextField)

        val hintString: String? =
            obtainStyledAttributes.getString(R.styleable.MaterialTextField_android_hint)
        val textString: String? =
            obtainStyledAttributes.getString(R.styleable.MaterialTextField_android_text)

        textInputLayout.hint = hintString
        textInputEditText.apply { setText(textString) }
        obtainStyledAttributes.recycle()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        textInputLayout.isEnabled = enabled
    }

    override fun setOnClickListener(l: OnClickListener?) =
        textInputEditText.setOnClickListener(InternalClickListener(l))

    fun setText(text: CharSequence?, hideIfEmpty: Boolean = true) =
        textInputEditText.setText(text ?: "")
            .also { if (text.isNullOrEmpty() && hideIfEmpty) visibility = View.GONE }

    fun setTextKeepState(text: CharSequence?, hideIfEmpty: Boolean = true) =
        textInputEditText.setTextKeepState(text ?: "")
            .also { if (text.isNullOrEmpty() && hideIfEmpty) visibility = View.GONE }

    fun setText(text: CharSequence?, type: TextView.BufferType, hideIfEmpty: Boolean = true) =
        textInputEditText.setText(text ?: "", type)
            .also { if (text.isNullOrEmpty() && hideIfEmpty) visibility = View.GONE }

    fun setText(text: CharArray?, start: Int, len: Int, hideIfEmpty: Boolean = true) =
        textInputEditText.setText(text, start, len)
            .also { if ((text == null || text.isEmpty()) && hideIfEmpty) visibility = View.GONE }

    fun setTextKeepState(
        text: CharSequence?, type: TextView.BufferType, hideIfEmpty: Boolean = true
    ) = textInputEditText.setTextKeepState(text ?: "", type)
        .also { if (text.isNullOrEmpty() && hideIfEmpty) visibility = View.GONE }

    fun setText(@StringRes resId: Int) =
        textInputEditText.setText(resId)

    fun setText(@StringRes resId: Int, type: TextView.BufferType) =
        textInputEditText.setText(resId, type)

    private inner class InternalClickListener(
        private val listener: OnClickListener?
    ) : OnClickListener {
        override fun onClick(view: View) {
            listener?.onClick(this@MaterialTextField)
        }
    }
}