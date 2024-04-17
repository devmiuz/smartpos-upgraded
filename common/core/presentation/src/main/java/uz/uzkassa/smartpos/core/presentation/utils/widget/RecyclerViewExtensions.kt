package uz.uzkassa.smartpos.core.presentation.utils.widget

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

val RecyclerView.ViewHolder.context: Context
    get() = itemView.context

fun RecyclerView.ViewHolder.getDrawable(@DrawableRes resId: Int) =
    AppCompatResources.getDrawable(context, resId)

fun RecyclerView.ViewHolder.getColor(@ColorRes resId: Int) =
    ContextCompat.getColor(context, resId)

fun RecyclerView.ViewHolder.getString(@StringRes resId: Int, vararg formatArgs: Any) =
    context.getString(resId, *formatArgs)