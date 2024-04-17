package uz.uzkassa.smartpos.core.presentation.widget.roundframelayout

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import uz.uzkassa.smartpos.core.presentation.R


class RoundFrameLayout(context: Context, attributeSet: AttributeSet?) : FrameLayout(context) {
    private val path = Path()
    private var leftTopRound: Float = 0f
    private var rightTopRound: Float = 0f
    private var leftBottomRound: Float = 0f
    private var rightBottomRound: Float = 0f
    private var round: Float = 0f

    init {
        val obtainStyledAttributes: TypedArray =
            getContext().obtainStyledAttributes(attributeSet, R.styleable.RoundFrameLayout)

        round = obtainStyledAttributes.getDimension(R.styleable.RoundFrameLayout_round, round)
        leftTopRound = obtainStyledAttributes.getDimension(
            R.styleable.RoundFrameLayout_leftTopRound,
            leftTopRound
        )
        rightTopRound = obtainStyledAttributes.getDimension(
            R.styleable.RoundFrameLayout_rightTopRound,
            rightTopRound
        )
        rightBottomRound = obtainStyledAttributes.getDimension(
            R.styleable.RoundFrameLayout_rightBottomRound,
            rightBottomRound
        )
        leftBottomRound = obtainStyledAttributes.getDimension(
            R.styleable.RoundFrameLayout_leftBottomRound,
            leftBottomRound
        )

        if (round != 0f) {
            leftTopRound = round
            rightTopRound = round
            rightBottomRound = round
            leftBottomRound = round
        }

        obtainStyledAttributes.recycle()
    }

    override fun dispatchDraw(canvas: Canvas) {
        val array: FloatArray = floatArrayOf(
            leftTopRound,
            leftTopRound,
            rightTopRound,
            rightTopRound,
            rightBottomRound,
            rightBottomRound,
            leftBottomRound,
            leftBottomRound
        )
        path.addRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            array,
            Path.Direction.CW
        )
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
    }

    override fun shouldDelayChildPressedState() = false
}