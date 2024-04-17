package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.decoration.space.entrust

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

internal class StaggeredGridLayoutManagerEntrust(
    leftRight: Int, topBottom: Int, mColor: Int
) : SpacesItemDecorationEntrust(leftRight, topBottom, mColor) {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager: StaggeredGridLayoutManager? =
            parent.layoutManager as StaggeredGridLayoutManager?
        val layoutParams: StaggeredGridLayoutManager.LayoutParams =
            view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val childPosition: Int = parent.getChildAdapterPosition(view)
        val spanCount: Int = layoutManager!!.spanCount
        val spanSize: Int = if (layoutParams.isFullSpan) layoutManager.spanCount else 1

        with(outRect) {
            if (layoutManager.orientation == GridLayoutManager.VERTICAL) {
                if (getSpanGroupIndex(childPosition, spanCount, spanSize) == 0) {
                    top = vertical
                }
                bottom = vertical
                if (layoutParams.isFullSpan) {
                    left = horizontal; right = horizontal
                } else {
                    left =
                        ((spanCount - layoutParams.spanIndex).toFloat() / spanCount * horizontal).toInt()
                    right = (horizontal.toFloat() * (spanCount + 1) / spanCount - left).toInt()
                }
            } else {
                if (getSpanGroupIndex(childPosition, spanCount, spanSize) == 0) {
                    left = horizontal
                }
                right = horizontal
                if (layoutParams.isFullSpan) {
                    top = vertical; bottom = vertical
                } else {
                    top =
                        ((spanCount - layoutParams.spanIndex).toFloat() / spanCount * vertical).toInt()
                    bottom = (vertical.toFloat() * (spanCount + 1) / spanCount - top).toInt()
                }
            }
        }
    }

    private fun getSpanGroupIndex(adapterPosition: Int, spanCount: Int, spanSize: Int): Int {
        var span = 0
        var group = 0
        repeat((0 until adapterPosition).count()) {
            span += spanSize
            if (span == spanCount) {
                span = 0; group++
            } else if (span > spanCount) {
                span = spanSize; group++
            }
        }
        if (span + spanSize > spanCount) {
            group++
        }
        return group
    }
}