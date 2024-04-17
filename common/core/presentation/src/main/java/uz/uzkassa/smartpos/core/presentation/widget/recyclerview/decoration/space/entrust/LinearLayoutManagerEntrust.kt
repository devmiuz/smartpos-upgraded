package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.decoration.space.entrust

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

internal class LinearLayoutManagerEntrust(leftRight: Int, topBottom: Int, mColor: Int) :
    SpacesItemDecorationEntrust(leftRight, topBottom, mColor) {

    public override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager: LinearLayoutManager? = parent.layoutManager as LinearLayoutManager?
        if (divider == null || layoutManager == null || layoutManager.childCount == 0) return

        var left: Int
        var right: Int
        var top: Int
        var bottom: Int
        val childCount: Int = parent.childCount

        if (layoutManager.orientation == RecyclerView.VERTICAL) {
            (0 until childCount - 1).forEach {
                val child: View = parent.getChildAt(it)
                val center: Float =
                    ((layoutManager.getTopDecorationHeight(child) + 1 - vertical) / 2).toFloat()
                left = layoutManager.getLeftDecorationWidth(child)
                right = parent.width - layoutManager.getLeftDecorationWidth(child)
                top = (child.bottom + center).toInt()
                bottom = top + vertical
                divider?.setBounds(left, top, right, bottom)
                divider?.draw(c)
            }
        } else {
            (0 until childCount - 1).forEach {
                val child: View = parent.getChildAt(it)
                val center: Float =
                    ((layoutManager.getLeftDecorationWidth(child) + 1 - horizontal) / 2).toFloat()
                left = (child.right + center).toInt()
                right = left + horizontal
                top = layoutManager.getTopDecorationHeight(child)
                bottom = parent.height - layoutManager.getTopDecorationHeight(child)
                divider?.setBounds(left, top, right, bottom)
                divider?.draw(c)
            }
        }

    }

    public override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager: LinearLayoutManager? = parent.layoutManager as LinearLayoutManager?
        with(outRect) {
            if (layoutManager!!.orientation == RecyclerView.VERTICAL) {
                if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) bottom =
                    vertical
                top = vertical; left = horizontal; right = horizontal
            } else {
                if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) right =
                    horizontal
                top = vertical
                left = horizontal
                bottom = vertical
            }
        }
    }
}