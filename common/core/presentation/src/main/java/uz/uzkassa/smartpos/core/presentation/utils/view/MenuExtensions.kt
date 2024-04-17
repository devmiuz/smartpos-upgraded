package uz.uzkassa.smartpos.core.presentation.utils.view

import android.view.MenuItem
import androidx.annotation.ColorInt

fun MenuItem.setTint(@ColorInt resId: Int) {
    icon?.let {
        it.mutate()
        it.setTint(resId)
    }
}