package uz.uzkassa.smartpos.feature.user.auth.presentation.features.cashier.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.user.auth.R

class PinCodeView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {
    private var listener: OnPinChangedListener? = null
    private var _pinCode: String = ""
        private set(value) {
            field = value
            repeat(6) {
                findViewWithTag<PinCodeViewCell>(it).isActive = it < value.length
            }
            if (value.length == 6) listener?.onPinCodeInputCompleted(value)
        }

    init {
        gravity = Gravity.CENTER
        repeat(6) {
            PinCodeViewCell(context).apply {
                tag = it
                val params: LayoutParams =
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        weight = 1.0f
                        if (it != 5)
                            rightMargin = context.resources.getDimensionPixelSize(R.dimen._10sdp)
                    }

                addView(this, params)
            }
        }
    }

    fun setPinCode(value: String) {
        _pinCode = TextUtils.replaceAllLetters(value)
    }

    fun setOnPinChangedListener(listener: (String) -> Unit) {
        this.listener = object : OnPinChangedListener {
            override fun onPinCodeInputCompleted(pinCode: String) =
                listener.invoke(pinCode)
        }
    }

    fun resetPinCode() {
        _pinCode = ""
    }

    interface OnPinChangedListener {
        fun onPinCodeInputCompleted(pinCode: String)
    }

    private class PinCodeViewCell @JvmOverloads constructor(
        context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
    ) : View(context, attributeSet, defStyleAttr) {
        private val colorWhite: Int = context.colorAccent
        private val paint: Paint = Paint().apply { isAntiAlias = true }
        private val size = context.resources.getDimensionPixelSize(R.dimen._8sdp)

        var isActive: Boolean = false
            set(value) {
                field = value
                paint.apply {
                    if (value) {
                        color = colorWhite
                        style = Paint.Style.FILL
                    } else {
                        color = Color.GRAY
                        style = Paint.Style.STROKE
                        strokeWidth =
                            context.resources.getDimensionPixelSize(R.dimen._2sdp).toFloat()
                    }
                }
                invalidate()
            }

        override fun onAttachedToWindow() {
            super.onAttachedToWindow()
            isActive = false
        }

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            val circleWidth: Float = (width / 2).toFloat()
            canvas?.drawCircle(circleWidth, circleWidth, circleWidth - size, paint)

            val style: Paint.Style = paint.style
            paint.style = Paint.Style.FILL

            canvas?.drawRect(
                0.0f,
                (height - size / 2).toFloat(),
                width.toFloat(),
                height.toFloat(),
                paint
            )

            paint.style = style
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            setMeasuredDimension(
                context.resources.getDimensionPixelSize(R.dimen._30sdp),
                context.resources.getDimensionPixelSize(R.dimen._40sdp)
            )
        }
    }
}