package uz.uzkassa.smartpos.core.presentation.utils.graphics

import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat

fun Drawable.setColorFilter(@ColorInt color: Int) =
    setColorFilter(color, BlendModeCompat.SRC_ATOP)

fun Drawable.setColorFilter(@ColorInt color: Int, mode: BlendModeCompat) {
    val colorFilter: ColorFilter? =
        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, mode)
    this.colorFilter = colorFilter
}
