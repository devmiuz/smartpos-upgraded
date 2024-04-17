package uz.uzkassa.smartpos.core.presentation.support.delegate.view

import android.view.MenuItem
import android.view.View
import androidx.annotation.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import uz.uzkassa.smartpos.core.presentation.utils.view.setTint

@Suppress("unused")
open class ToolbarDelegate(target: LifecycleOwner) : ViewDelegate<Toolbar>(target) {

    fun setTitle(@StringRes resourceId: Int) {
        view?.setTitle(resourceId)
    }

    @Suppress("UsePropertyAccessSyntax")
    fun setTitle(title: CharSequence) {
        view?.setTitle(title)
    }

    fun setSubtitle(@StringRes resourceId: Int) {
        view?.setSubtitle(resourceId)
    }

    @Suppress("UsePropertyAccessSyntax")
    fun setSubtitle(title: CharSequence) {
        view?.setSubtitle(title)
    }

    fun setNavigationIcon(@DrawableRes resourceId: Int) {
        view?.setNavigationIcon(resourceId)
    }

    fun setNavigationIcon(
        @DrawableRes drawableResourceId: Int,
        @ColorRes colorResourceId: Int
    ) {
        view?.apply {
            setNavigationIcon(drawableResourceId)
            navigationIcon?.setTint(ContextCompat.getColor(context, colorResourceId))
        }
    }

    fun setNavigationOnClickListener(onClickListener: View.OnClickListener) {
        view?.setNavigationOnClickListener(onClickListener)
    }

    @Suppress("PROTECTED_CALL_FROM_PUBLIC_INLINE")
    inline fun setNavigationOnClickListener(crossinline onClick: (view: View) -> Unit) {
        view?.setNavigationOnClickListener { onClick(it) }
    }

    inline fun setNavigation(@DrawableRes resourceId: Int, crossinline onClick: () -> Unit) {
        setNavigationIcon(resourceId)
        setNavigationOnClickListener(View.OnClickListener { onClick() })
    }

    inline fun setNavigation(
        @DrawableRes drawableResourceId: Int,
        @ColorRes colorResourceId: Int,
        crossinline onClick: () -> Unit
    ) {
        setNavigationIcon(drawableResourceId, colorResourceId)
        setNavigationOnClickListener(View.OnClickListener { onClick() })
    }

    fun inflateMenu(@MenuRes menuResId: Int, listener: Toolbar.OnMenuItemClickListener) {
        view?.apply {
            clearMenu()
            inflateMenu(menuResId)
            setOnMenuItemClickListener(listener)
        }
    }

    fun findMenuItemById(@IdRes menuId: Int): MenuItem? =
        view?.menu?.findItem(menuId)

    fun setTintMenuItemById(@IdRes menuResId: Int, @ColorInt colorResId: Int) {
        findMenuItemById(menuResId)?.setTint(colorResId)
    }

    fun clearMenu() {
        view?.menu?.clear()
    }

    fun removeNavigation() {
        view?.apply { navigationIcon = null; setNavigationOnClickListener(null) }
    }

}