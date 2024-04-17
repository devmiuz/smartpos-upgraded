package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.decoration.space.entrust

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal abstract class SpacesItemDecorationEntrust(
    protected var horizontal: Int, protected var vertical: Int, color: Int
) {
    protected var divider: Drawable? = null

    init {
        if (color != 0) divider = ColorDrawable(color)
    }

    internal abstract fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State)

    internal abstract fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    )

}