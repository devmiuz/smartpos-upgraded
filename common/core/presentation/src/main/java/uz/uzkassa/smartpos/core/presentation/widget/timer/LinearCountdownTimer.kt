package uz.uzkassa.smartpos.core.presentation.widget.timer

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.presentation.utils.widget.ResizeWidthAnimation

class LinearCountdownTimer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {

    private val textView: TextView
    private val layout: FrameLayout
    private val parentLayout: LinearLayout

    var percent: Int = 0
        set(value) {

            when {
                value < 0 -> update(0)
                value > 100 -> update(100)
                else -> update(value)
            }
        }
    var time: String? = null
        set (value) {
            textView.text = value
            field = value
        }
    init {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.widget_countdown_timer, this, true)
        textView = view.findViewById(R.id.tvTimer)
        layout = view.findViewById(R.id.flTicker)
        parentLayout = view.findViewById(R.id.llParent)
    }

    // private methods
    private fun update(percent: Int = 0) {
        post {
            val width = parentLayout.width * percent.toDouble() / 100.0
            val anim = ResizeWidthAnimation(layout, width.toInt())
            anim.duration = 300
            layout.startAnimation(anim)
        }
    }

}