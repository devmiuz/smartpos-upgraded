package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.decoration.space.entrust

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

internal class GridLayoutManagerEntrust(
    leftRight: Int, topBottom: Int, mColor: Int
) : SpacesItemDecorationEntrust(leftRight, topBottom, mColor) {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager: GridLayoutManager? = parent.layoutManager as GridLayoutManager?
        val lookup: GridLayoutManager.SpanSizeLookup? = layoutManager?.spanSizeLookup

        if (divider == null || layoutManager == null || layoutManager.childCount == 0 || lookup == null) return

        val spanCount: Int = layoutManager.spanCount
        var left: Int
        var right: Int
        var top: Int
        var bottom: Int
        val childCount: Int = parent.childCount

        if (layoutManager.orientation == RecyclerView.VERTICAL) {
            (0 until childCount).forEach {
                val child: View = parent.getChildAt(it)

                val centerLeft: Float =
                    ((layoutManager.getLeftDecorationWidth(child) +
                            layoutManager.getRightDecorationWidth(child)).toFloat() *
                            spanCount / (spanCount + 1) + 1 - horizontal) / 2

                val centerTop: Float =
                    ((layoutManager.getBottomDecorationHeight(child) + 1 - vertical) / 2).toFloat()
                val position: Int = parent.getChildAdapterPosition(child)
                val spanSize: Int = lookup.getSpanSize(position)
                val spanIndex: Int = lookup.getSpanIndex(position, layoutManager.spanCount)
                val isFirst: Boolean =
                    layoutManager.spanSizeLookup.getSpanGroupIndex(position, spanCount) == 0

                if (!isFirst && spanIndex == 0) {
                    left = layoutManager.getLeftDecorationWidth(child)
                    right = parent.width - layoutManager.getLeftDecorationWidth(child)
                    top = (child.top - centerTop).toInt() - vertical
                    bottom = top + vertical
                    divider?.setBounds(left, top, right, bottom)
                    divider?.draw(c)
                }
                val isRight: Boolean = spanIndex + spanSize == spanCount
                if (!isRight) {
                    left = (child.right + centerLeft).toInt()
                    right = left + horizontal
                    top = child.top
                    if (!isFirst) {
                        top -= centerTop.toInt()
                    }
                    bottom = (child.bottom + centerTop).toInt()
                    divider?.setBounds(left, top, right, bottom)
                    divider?.draw(c)
                }
            }
        } else {
            (0 until childCount).forEach {
                val child: View = parent.getChildAt(it)

                val centerLeft: Float =
                    ((layoutManager.getRightDecorationWidth(child) + 1 - horizontal) / 2).toFloat()
                val centerTop: Float =
                    ((layoutManager.getTopDecorationHeight(child) +
                            layoutManager.getBottomDecorationHeight(child)).toFloat() *
                            spanCount / (spanCount + 1) - vertical) / 2

                val position: Int = parent.getChildAdapterPosition(child)
                val spanSize: Int = lookup.getSpanSize(position)
                val spanIndex: Int = lookup.getSpanIndex(position, layoutManager.spanCount)
                val isFirst: Boolean =
                    layoutManager.spanSizeLookup.getSpanGroupIndex(position, spanCount) == 0

                if (!isFirst && spanIndex == 0) {
                    left = (child.left - centerLeft).toInt() - horizontal
                    right = left + horizontal
                    top = layoutManager.getRightDecorationWidth(child)
                    bottom = parent.height - layoutManager.getTopDecorationHeight(child)
                    divider?.setBounds(left, top, right, bottom)
                    divider?.draw(c)
                }

                if (spanIndex + spanSize != spanCount) {
                    left = child.left
                    if (!isFirst) {
                        left -= centerLeft.toInt()
                    }
                    right = (child.right + centerTop).toInt()
                    top = (child.bottom + centerLeft).toInt()
                    bottom = top + horizontal
                    divider?.setBounds(left, top, right, bottom)
                    divider?.draw(c)
                }
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager: GridLayoutManager? = parent.layoutManager as GridLayoutManager?
        val layoutParams: GridLayoutManager.LayoutParams =
            view.layoutParams as GridLayoutManager.LayoutParams
        val childPosition: Int = parent.getChildAdapterPosition(view)
        val spanCount: Int = layoutManager!!.spanCount

        with(outRect) {
            if (layoutManager.orientation == RecyclerView.VERTICAL) {
                if (layoutManager.spanSizeLookup.getSpanGroupIndex(childPosition, spanCount) == 0) {
                    top = vertical
                }
                bottom = vertical
                if (layoutParams.spanSize == spanCount) {
                    left = horizontal; right = horizontal
                } else {
                    left =
                        ((spanCount - layoutParams.spanIndex).toFloat() / spanCount * horizontal).toInt()
                    right = (horizontal.toFloat() * (spanCount + 1) / spanCount - left).toInt()
                }
            } else {
                if (layoutManager.spanSizeLookup.getSpanGroupIndex(childPosition, spanCount) == 0) {
                    left = horizontal
                }
                right = horizontal
                if (layoutParams.spanSize == spanCount) {
                    top = vertical
                    bottom = vertical
                } else {
                    top =
                        ((spanCount - layoutParams.spanIndex).toFloat() / spanCount * vertical).toInt()
                    bottom = (vertical.toFloat() * (spanCount + 1) / spanCount - top).toInt()
                }
            }
        }
    }


}