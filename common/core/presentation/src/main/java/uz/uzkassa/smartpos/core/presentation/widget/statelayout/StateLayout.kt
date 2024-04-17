package uz.uzkassa.smartpos.core.presentation.widget.statelayout

import android.R.attr
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.PorterDuff.Mode.SRC_IN
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import uz.uzkassa.smartpos.core.presentation.R
import java.net.ConnectException

@Suppress("MemberVisibilityCanBePrivate", "unused")
@SuppressLint("RestrictedApi")
class StateLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), OnClickListener {

    private var errorButtonClickListener: OnErrorButtonClickListener? = null

    private var successViewGroup: View? = null

    private val loadingLinearLayout: LinearLayout
    private val emptyLinearLayout: LinearLayout
    private val errorLinearLayout: LinearLayout

    private val emptyImageView: AppCompatImageView
    private val errorImageView: AppCompatImageView

    private val emptyTitleTextView: AppCompatTextView
    private val errorTitleTextView: AppCompatTextView
    private val errorMessageTextView: AppCompatTextView

    private val errorRectangleButton: MaterialButton
    private val errorRoundButton: FloatingActionButton

    init {
        val layoutRes: Int =
            if (orientation == VERTICAL) R.layout.core_presentation_widget_state_layout_vertical
            else R.layout.core_presentation_widget_state_layout_horizontal

        LayoutInflater.from(context).inflate(layoutRes, this, true)

        loadingLinearLayout =
            findViewById(R.id.core_presentation_widget_state_layout_loading_linearlayout)
        emptyLinearLayout =
            findViewById(R.id.core_presentation_widget_state_layout_empty_linearlayout)
        errorLinearLayout =
            findViewById(R.id.core_presentation_widget_state_layout_error_linearlayout)

        emptyImageView = findViewById(android.R.id.icon)
        emptyTitleTextView = findViewById(android.R.id.empty)

        errorMessageTextView = findViewById(android.R.id.text2)

        errorImageView = findViewById(android.R.id.icon1)
        errorTitleTextView = findViewById(android.R.id.text1)

        errorRectangleButton = findViewById(android.R.id.button1)
        errorRoundButton = findViewById(android.R.id.button2)

        try {

            val attributes: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.StateLayout, defStyleAttr, 0)

            var colorResId: Int? = null

            var errorRoundIconColorResId: Int? = null

            if (attributes.hasValue(R.styleable.StateLayout_icon_background_tint)) {
                colorResId =
                    attributes.getResourceId(R.styleable.StateLayout_icon_background_tint, 0)
            }

            if (attributes.hasValue(R.styleable.StateLayout_error_round_icon_background_tint)) {
                errorRoundIconColorResId =
                    attributes.getResourceId(
                        R.styleable.StateLayout_error_round_icon_background_tint,
                        0
                    )
            }

            if (attributes.hasValue(R.styleable.StateLayout_empty_icon)) {
                val drawableResId: Int =
                    attributes.getResourceId(R.styleable.StateLayout_empty_icon, 0)
                emptyImageView.setImageResource(drawableResId)
            }

            if (attributes.hasValue(R.styleable.StateLayout_error_icon)) {
                val drawableResId: Int =
                    attributes.getResourceId(R.styleable.StateLayout_error_icon, 0)
                errorImageView.setImageResource(drawableResId)
            }

            if (attributes.hasValue(R.styleable.StateLayout_error_round_icon)) {
                val drawableResId: Int =
                    attributes.getResourceId(R.styleable.StateLayout_error_round_icon, 0)
                errorRoundButton.setImageResource(drawableResId)
            }

            if (attributes.hasValue(R.styleable.StateLayout_empty_title)) {
                emptyTitleTextView.text = attributes.getString(R.styleable.StateLayout_empty_title)
            }

            if (attributes.hasValue(R.styleable.StateLayout_error_title)) {
                errorTitleTextView.text = attributes.getString(R.styleable.StateLayout_error_title)
            }

            if (attributes.hasValue(R.styleable.StateLayout_error_message)) {
                errorMessageTextView.text =
                    attributes.getString(R.styleable.StateLayout_error_message)
            }

            if (attributes.hasValue(R.styleable.StateLayout_error_button_title)) {
                errorRectangleButton.text =
                    attributes.getString(R.styleable.StateLayout_error_button_title)
            }

            if (attributes.hasValue(R.styleable.StateLayout_error_button_style)) {
                val styleInt: Int = attributes.getInt(R.styleable.StateLayout_error_button_style, 0)

                errorRectangleButton.visibility = if (styleInt == 0) View.VISIBLE else View.GONE
                errorRoundButton.visibility = if (styleInt == 1) View.VISIBLE else View.GONE
            }

            if (attributes.hasValue(R.styleable.StateLayout_empty_icon_visible)) {
                val isVisible: Boolean =
                    attributes.getBoolean(R.styleable.StateLayout_empty_icon_visible, true)
                emptyImageView.visibility = if (isVisible) View.VISIBLE else View.GONE
            }

            if (attributes.hasValue(R.styleable.StateLayout_error_title_visible)) {
                val isVisible: Boolean =
                    attributes.getBoolean(R.styleable.StateLayout_error_title_visible, true)
                errorTitleTextView.visibility = if (isVisible) View.VISIBLE else View.GONE
            }

            if (attributes.hasValue(R.styleable.StateLayout_error_message_visible)) {
                val isVisible: Boolean =
                    attributes.getBoolean(R.styleable.StateLayout_error_message_visible, true)
                errorMessageTextView.visibility = if (isVisible) View.VISIBLE else View.GONE
            }

            if (attributes.hasValue(R.styleable.StateLayout_error_button_visible)) {
                val isVisible: Boolean =
                    attributes.getBoolean(R.styleable.StateLayout_error_button_visible, true)
                errorRectangleButton.visibility = if (isVisible) View.VISIBLE else View.GONE
            }

            if (orientation == VERTICAL) {
                colorResId?.let {
                    emptyImageView.setColorFilter(ContextCompat.getColor(context, it), SRC_IN)
                    errorImageView?.setColorFilter(ContextCompat.getColor(context, it), SRC_IN)
                }
            }

            errorRoundIconColorResId?.let { errorRoundButton.setColorFilter(it) }

            attributes.recycle()

            if (errorRectangleButton.visibility == View.VISIBLE) errorRectangleButton.setOnClickListener(
                this
            )
            if (errorRoundButton.visibility == View.VISIBLE) {
                errorLinearLayout.setOnClickListener(this)
            }

        } catch (e: Exception) {
        }
    }

    val isLoading: Boolean
        get() = loadingLinearLayout.visibility == View.VISIBLE

    val isSuccess: Boolean
        get() = successViewGroup?.visibility == View.VISIBLE

    val isEmpty: Boolean
        get() = emptyLinearLayout.visibility == View.VISIBLE

    val isError: Boolean
        get() = errorLinearLayout.visibility == View.VISIBLE

    fun setOnErrorButtonClickListener(onClick: () -> Unit) {
        errorButtonClickListener = object : OnErrorButtonClickListener {
            override fun onErrorButtonClick(stateLayout: StateLayout) = onClick.invoke()
        }
    }

    fun setOnErrorButtonClickListener(listener: OnErrorButtonClickListener) {
        errorButtonClickListener = listener
    }

    fun clear() {
        loadingLinearLayout.visibility = View.GONE
        successViewGroup?.visibility = View.GONE
        emptyLinearLayout.visibility = View.GONE
        errorLinearLayout.visibility = View.GONE
    }

    fun setToLoading() {
        successViewGroup?.visibility = View.GONE
        emptyLinearLayout.visibility = View.GONE
        errorLinearLayout.visibility = View.GONE
        loadingLinearLayout.visibility = View.VISIBLE
    }

    fun setToSuccess() {
        loadingLinearLayout.visibility = View.GONE
        emptyLinearLayout.visibility = View.GONE
        errorLinearLayout.visibility = View.GONE
        successViewGroup?.visibility = View.VISIBLE
    }

    fun setToEmpty() {
        loadingLinearLayout.visibility = View.GONE
        successViewGroup?.visibility = View.GONE
        errorLinearLayout.visibility = View.GONE
        emptyLinearLayout.visibility = View.VISIBLE
    }

    fun setToEmpty(@DrawableRes drawableResId: Int?, @StringRes stringResId: Int?) {
        loadingLinearLayout.visibility = View.GONE
        successViewGroup?.visibility = View.GONE
        errorLinearLayout.visibility = View.GONE
        emptyLinearLayout.visibility = View.VISIBLE

        drawableResId?.let { emptyImageView.setImageResource(it) }
        stringResId?.let { emptyTitleTextView.setText(it) }
    }

    fun setToError() {
        loadingLinearLayout.visibility = View.GONE
        successViewGroup?.visibility = View.GONE
        emptyLinearLayout.visibility = View.GONE
        errorLinearLayout.visibility = View.VISIBLE
    }

    fun setToError(throwable: Throwable?) {
        val iconResId: Int = when (throwable) {
            is ConnectException -> R.drawable.core_presentation_vector_drawable_no_connection
            else -> R.drawable.core_presentation_vector_drawable_unknown_error
        }

        val title: String = context.getString(R.string.core_presentation_common_error)

        val message: String = when (throwable) {
            is ConnectException -> context.getString(R.string.core_presentation_common_error_connection_message)
            else -> context.getString(R.string.core_presentation_common_error_unknown_message)
        }

        errorImageView.setImageResource(iconResId)
        errorRoundButton.setImageResource(iconResId)
        errorTitleTextView.text = title
        errorMessageTextView.text = message

        setToError()
    }


    override fun addView(child: View?) {
        if (childCount > 3) throw IllegalStateException("StateLayout can host only one direct child")
        if (child != loadingLinearLayout && child != emptyLinearLayout && child != errorLinearLayout)
            successViewGroup = child
        super.addView(child)
    }

    override fun addView(child: View?, index: Int) {
        if (childCount > 3) throw IllegalStateException("StateLayout can host only one direct child")
        if (child != loadingLinearLayout && child != emptyLinearLayout && child != errorLinearLayout)
            successViewGroup = child
        super.addView(child, index)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (childCount > 3) throw IllegalStateException("StateLayout can host only one direct child")
        if (child != loadingLinearLayout && child != emptyLinearLayout && child != errorLinearLayout)
            successViewGroup = child
        super.addView(child, params)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (childCount > 3) throw IllegalStateException("StateLayout can host only one direct child")
        if (child != loadingLinearLayout && child != emptyLinearLayout && child != errorLinearLayout)
            successViewGroup = child
        super.addView(child, index, params)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        if (childCount > 3) throw IllegalStateException("StateLayout can host only one direct child")
        if (child != loadingLinearLayout && child != emptyLinearLayout && child != errorLinearLayout)
            successViewGroup = child
        super.addView(child, width, height)
    }

    override fun addViewInLayout(
        child: View?, index: Int, params: ViewGroup.LayoutParams?
    ): Boolean {
        if (childCount > 3) throw IllegalStateException("StateLayout can host only one direct child")
        if (child != loadingLinearLayout && child != emptyLinearLayout && child != errorLinearLayout)
            successViewGroup = child
        return super.addViewInLayout(child, index, params)
    }

    override fun addViewInLayout(
        child: View?, index: Int, params:
        ViewGroup.LayoutParams?, preventRequestLayout: Boolean
    ): Boolean {
        if (childCount > 3) throw IllegalStateException("StateLayout can host only one direct child")
        if (child != loadingLinearLayout && child != emptyLinearLayout && child != errorLinearLayout)
            successViewGroup = child
        return super.addViewInLayout(child, index, params, preventRequestLayout)
    }

    override fun setOrientation(orientation: Int) {
        super.setOrientation(orientation)
        if (orientation == HORIZONTAL) {
            val typedValue = TypedValue()

            if (context.theme.resolveAttribute(attr.actionBarSize, typedValue, true)) {
                val layoutParams =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                layoutParams.height =
                    TypedValue.complexToDimensionPixelSize(
                        typedValue.data,
                        resources.displayMetrics
                    )
                setLayoutParams(layoutParams)
            }
        }

    }

    @Deprecated("")
    override fun onClick(view: View) {
        when (view) {
            errorLinearLayout, errorRectangleButton -> {
                errorButtonClickListener?.onErrorButtonClick(this)
            }
        }
    }

    interface OnErrorButtonClickListener {
        fun onErrorButtonClick(stateLayout: StateLayout)
    }
}