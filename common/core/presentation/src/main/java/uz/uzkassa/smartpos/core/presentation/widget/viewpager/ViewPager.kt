package uz.uzkassa.smartpos.core.presentation.widget.viewpager

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

@Suppress("unused", "MemberVisibilityCanBePrivate")
open class ViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    var isPagingEnabled = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean =
        isPagingEnabled && super.onTouchEvent(ev)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean =
        isPagingEnabled && super.onInterceptTouchEvent(ev)
}