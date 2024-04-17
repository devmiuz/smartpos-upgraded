package uz.uzkassa.smartpos.core.presentation.widget.roundbuttonlayout

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.presentation.widget.roundframelayout.RoundFrameLayout

class RoundButtonLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val roundFrameLayout: RoundFrameLayout
    private val backgroundView: View
    private val textView: AppCompatTextView

    init {
        val view: View =
            LayoutInflater
                .from(context)
                .inflate(R.layout.core_presentation_layout_widget_roundbuttonlayout, this, true)

        roundFrameLayout = (view as ViewGroup).getChildAt(0) as RoundFrameLayout
        backgroundView = roundFrameLayout.getChildAt(0)
        textView = roundFrameLayout.getChildAt(1) as AppCompatTextView

        val obtainStyledAttributes: TypedArray =
            getContext().obtainStyledAttributes(attrs, R.styleable.RoundButtonLayout)

        textView.text = obtainStyledAttributes.getString(R.styleable.RoundButtonLayout_android_text)

        obtainStyledAttributes.recycle()
    }
}