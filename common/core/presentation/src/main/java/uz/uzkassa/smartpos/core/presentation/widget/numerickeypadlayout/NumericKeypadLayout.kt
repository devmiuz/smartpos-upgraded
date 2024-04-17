package uz.uzkassa.smartpos.core.presentation.widget.numerickeypadlayout

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.core.presentation.utils.widget.text
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import java.math.BigDecimal

class NumericKeypadLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener, View.OnLongClickListener {

    private var backgroundTint: Int
    private var textColor: Int

    private val dimension5: Float by lazy { context.resources.getDimension(R.dimen._5sdp) }
    private val dimension10: Float by lazy { context.resources.getDimension(R.dimen._10sdp) }
    private val dimension16: Float by lazy { context.resources.getDimension(R.dimen._16sdp) }

    private val selectableItemBackgroundId: Int by lazy {
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        return@lazy outValue.resourceId
    }

    private var defaultValue: String = "0"
    private var enableDefaultValue: Boolean = true
    private var maxDecimalLength: Int = Int.MAX_VALUE
    private var maxLength: Int = Int.MAX_VALUE
    private var startLengthLimit: Int = 0
    private var value: String = ""

    private var listener: OnKeypadValueChangedListener? = null

    init {
        val tableLayout: TableLayout = TableLayout(context, attrs).apply {
            isMeasureWithLargestChildEnabled = true
            isShrinkAllColumns = true
            isStretchAllColumns = true
            layoutParams =
                TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            orientation = TableLayout.VERTICAL
            setBackgroundColor(Color.WHITE)

            val obtainStyledAttributes: TypedArray =
                getContext().obtainStyledAttributes(attrs, R.styleable.NumericKeypadLayout)

            backgroundTint =
                ContextCompat.getColor(
                    context,
                    obtainStyledAttributes.getResourceId(
                        R.styleable.NumericKeypadLayout_android_backgroundTint,
                        R.color.colorDark
                    )
                )
            defaultValue =
                obtainStyledAttributes.getString(R.styleable.NumericKeypadLayout_defaultValue)
                    ?: "0"
            enableDefaultValue =
                obtainStyledAttributes.getBoolean(
                    R.styleable.NumericKeypadLayout_enableDefaultValue,
                    true
                )
            maxDecimalLength =
                obtainStyledAttributes.getInteger(
                    R.styleable.NumericKeypadLayout_maxDecimalLength,
                    Int.MAX_VALUE
                )
            maxLength =
                obtainStyledAttributes.getInteger(
                    R.styleable.NumericKeypadLayout_android_maxLength,
                    Int.MAX_VALUE
                )
            textColor =
                ContextCompat.getColor(
                    context,
                    obtainStyledAttributes.getResourceId(
                        R.styleable.NumericKeypadLayout_android_textColor,
                        android.R.color.white
                    )
                )

            val firstTextButtonString: String? =
                obtainStyledAttributes.getString(R.styleable.NumericKeypadLayout_firstTextButton)
            val secondTextButtonString: String? =
                obtainStyledAttributes.getString(R.styleable.NumericKeypadLayout_secondTextButton)
            val thirdTextButtonString: String? =
                obtainStyledAttributes.getString(R.styleable.NumericKeypadLayout_thirdTextButton)
            val fourthTextButtonString: String? =
                obtainStyledAttributes.getString(R.styleable.NumericKeypadLayout_fourthTextButton)

            obtainStyledAttributes.recycle()

            addView(
                getTableRowTextLine(
                    context,
                    attrs,
                    defStyleAttr,
                    "1",
                    "2",
                    "3",
                    firstTextButtonString
                )
            )
            addView(
                getTableRowTextLine(
                    context,
                    attrs,
                    defStyleAttr,
                    "4",
                    "5",
                    "6",
                    secondTextButtonString
                )
            )
            addView(
                getTableRowTextLine(
                    context,
                    attrs,
                    defStyleAttr,
                    "7",
                    "8",
                    "9",
                    thirdTextButtonString
                )
            )
            addView(
                getTableRowTextWithImageLine(
                    context,
                    attrs,
                    defStyleAttr,
                    TAG_IMAGE_DELETE,
                    R.drawable.core_presentation_vector_drawable_backspace_outline,
                    ".",
                    "0",
                    fourthTextButtonString
                )
            )
        }

        addView(tableLayout)
    }

    fun setOnKeypadValueChangedListener(listener: OnKeypadValueChangedListener) {
        this.listener = listener
    }

    fun setValue(value: BigDecimal) =
        setValue(value.toString())

    fun setValue(double: Double) =
        setValue(double.toString())

    fun setValue(value: String) {
        this.value = value
    }

    override fun onClick(view: View) {
        if (view is AppCompatTextView) {
            val text: String = view.text()
            if (view.tag != TAG_TEXT_ADDITIONAL) {
                val afterDecimal: String? = value.split('.').getOrNull(1)
                if (afterDecimal?.length == maxDecimalLength) return
                if (afterDecimal != null && text == ".") return

                when {
                    value.isEmpty() ->
                        value = if (text == ".") "0." else text
                    value == ".0" -> {
                        val oldValue: String = value.replace(".0", "")
                        value = if (text == ".") "$oldValue." else oldValue + text
                    }
                    value.endsWith(".") ->
                        if (text == ".") return else value += text
                    value.startsWith("0") && !value.contains(".") && text != "." ->
                        value = value.substring(1, value.length) + text
                    value.startsWith(".") ->
                        value = value.substring(1, value.length) + "0$text"
                    else -> value += text
                }

                listener?.onKeypadValueChanged(value, value.length == maxLength + 1)

            } else listener?.onAdditionalButtonClicked(TextUtils.replaceAllLetters(text, true))
        }

        if (view is AppCompatImageView && view.tag == TAG_IMAGE_DELETE) {
            if (value.isNotEmpty() && value.length != startLengthLimit) {
                value = value.substring(0, value.length - 1)
                listener?.onKeypadValueChanged(
                    value = if (value.isEmpty() && enableDefaultValue) defaultValue else value,
                    isCompleted = value.length == maxLength + 1
                )
            }
        }
    }

    override fun onLongClick(view: View): Boolean {
        if (view is AppCompatImageView && view.tag == TAG_IMAGE_DELETE) {
            clear()
            return true
        }
        return false
    }

    fun clear() {
        value = value.substring(0, value.length - (value.length - startLengthLimit))
        listener?.onKeypadValueChanged(if (enableDefaultValue) defaultValue else "", false)
    }

    private fun getTableRowTextLine(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        vararg buttonValues: String?
    ): TableRow =
        TableRow(context, attrs).apply {
            addFrameLayout(
                getTextFrameLayout(
                    context,
                    attrs,
                    defStyleAttr,
                    backgroundTint,
                    dimension10,
                    null,
                    buttonValues.getOrNull(0)
                )
            )
            addFrameLayout(
                getTextFrameLayout(
                    context,
                    attrs,
                    defStyleAttr,
                    backgroundTint,
                    dimension10,
                    null,
                    buttonValues.getOrNull(1)
                )
            )
            addFrameLayout(
                getTextFrameLayout(
                    context = context,
                    attrs = attrs,
                    defStyleAttr = defStyleAttr,
                    backgroundColor = backgroundTint,
                    textSize = dimension10,
                    textTag = null,
                    text = buttonValues.getOrNull(2)
                )
            )
            buttonValues.getOrNull(3)?.let {
                addFrameLayout(
                    getTextFrameLayout(
                        context = context,
                        attrs = attrs,
                        defStyleAttr = defStyleAttr,
                        backgroundColor = context.colorAccent,
                        textSize = dimension5,
                        textTag = TAG_TEXT_ADDITIONAL,
                        text = it
                    )
                )
            }
        }

    @Suppress("SameParameterValue")
    private fun getTableRowTextWithImageLine(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        imageTag: Int?,
        drawableResId: Int,
        vararg buttonValues: String?
    ): TableRow = TableRow(context, attrs).apply {
        addFrameLayout(
            getTextFrameLayout(
                context = context,
                attrs = attrs,
                defStyleAttr = defStyleAttr,
                backgroundColor = backgroundTint,
                textSize = dimension10,
                textTag = null,
                text = buttonValues.getOrNull(0)
            )
        )
        addFrameLayout(
            getTextFrameLayout(
                context = context,
                attrs = attrs,
                defStyleAttr = defStyleAttr,
                backgroundColor = backgroundTint,
                textSize = dimension10,
                textTag = null,
                text = buttonValues.getOrNull(1)
            )
        )
        addFrameLayout(
            getImageFrameLayout(
                context = context,
                attrs = attrs,
                defStyleAttr = defStyleAttr,
                backgroundColor = backgroundTint,
                imageTag = imageTag,
                drawableResId = drawableResId
            )
        )
        buttonValues.getOrNull(2)?.let {
            addFrameLayout(
                getTextFrameLayout(
                    context = context,
                    attrs = attrs,
                    defStyleAttr = defStyleAttr,
                    backgroundColor = context.colorAccent,
                    textSize = dimension5,
                    textTag = TAG_TEXT_ADDITIONAL,
                    text = it
                )
            )
        }
    }

    private fun getTextFrameLayout(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        backgroundColor: Int,
        textSize: Float,
        textTag: Int?,
        text: String?
    ): FrameLayout {
        return FrameLayout(context, attrs, defStyleAttr).apply {
            setBackgroundColor(backgroundColor)
            AppCompatTextView(context, attrs, defStyleAttr)
                .apply {
                    dimension10.toInt().let { setPadding(it, it, it, it) }
                    ellipsize = android.text.TextUtils.TruncateAt.END
                    gravity = Gravity.CENTER
                    isSingleLine = true
                    tag = textTag
                    this.textSize = textSize
                    setBackgroundResource(selectableItemBackgroundId)
                    setOnClickListener(this@NumericKeypadLayout)
                    setTextColor(textColor)
                    setText(text)
                }
                .also { addView(it) }
        }
    }

    private fun getImageFrameLayout(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        backgroundColor: Int,
        imageTag: Int?,
        drawableResId: Int
    ): FrameLayout {
        return FrameLayout(context, attrs, defStyleAttr).apply {
            setBackgroundColor(backgroundColor)
            AppCompatImageView(context, attrs, defStyleAttr)
                .apply {
                    tag = imageTag
                    dimension16.toInt().let { setPadding(it, it, it, it) }
                    setBackgroundResource(selectableItemBackgroundId)
                    setImageResource(drawableResId)
                    setColorFilter(textColor)
                    setOnClickListener(this@NumericKeypadLayout)
                    setOnLongClickListener(this@NumericKeypadLayout)
                }
                .also { addView(it) }
        }
    }

    private fun TableRow.addFrameLayout(layout: FrameLayout) {
        addView(layout)
        layout.layoutParams = TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 1F)
            .also { params -> 1.let { params.setMargins(it, it, it, it) } }
    }

    interface OnKeypadValueChangedListener {
        fun onKeypadValueChanged(value: String, isCompleted: Boolean)
        fun onAdditionalButtonClicked(value: String)
    }

    private companion object {
        const val TAG_IMAGE_DELETE: Int = 0
        const val TAG_TEXT_ADDITIONAL: Int = 1
    }
}