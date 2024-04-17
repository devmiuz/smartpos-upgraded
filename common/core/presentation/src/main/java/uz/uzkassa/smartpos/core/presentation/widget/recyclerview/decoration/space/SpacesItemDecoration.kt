package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.decoration.space

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.decoration.space.entrust.GridLayoutManagerEntrust
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.decoration.space.entrust.LinearLayoutManagerEntrust
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.decoration.space.entrust.SpacesItemDecorationEntrust
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.decoration.space.entrust.StaggeredGridLayoutManagerEntrust

class SpacesItemDecoration(
    @DimenRes private val verticalDimenResId: Int,
    @DimenRes private val horizontalDimenResId: Int,
    @ColorRes private val colorResId: Int = 0
) : RecyclerView.ItemDecoration() {

    private var spacesItemDecorationEntrust: SpacesItemDecorationEntrust? = null

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (spacesItemDecorationEntrust == null)
            spacesItemDecorationEntrust =
                getSpacesItemDecorationEntrust(parent.context, parent.layoutManager)
        spacesItemDecorationEntrust?.onDraw(c, parent, state)
        super.onDraw(c, parent, state)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (spacesItemDecorationEntrust == null)
            spacesItemDecorationEntrust =
                getSpacesItemDecorationEntrust(parent.context, parent.layoutManager)
        spacesItemDecorationEntrust?.getItemOffsets(outRect, view, parent, state)
    }

    @Synchronized
    private fun getSpacesItemDecorationEntrust(
        context: Context, layoutManager: RecyclerView.LayoutManager?
    ): SpacesItemDecorationEntrust {
        val vertical: Int = context.resources.getDimension(verticalDimenResId).toInt()
        val horizontal: Int = context.resources.getDimension(horizontalDimenResId).toInt()
        val color: Int = if (colorResId != 0) ContextCompat.getColor(context, colorResId) else 0

        return when (layoutManager) {
            is GridLayoutManager -> GridLayoutManagerEntrust(horizontal, vertical, color)
            is StaggeredGridLayoutManager -> StaggeredGridLayoutManagerEntrust(
                horizontal,
                vertical,
                color
            )
            else -> LinearLayoutManagerEntrust(horizontal, vertical, color)
        }
    }

}