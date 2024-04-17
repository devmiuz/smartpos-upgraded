package uz.uzkassa.smartpos.core.presentation.support.delegate.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.*
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

open class DrawerLayoutDelegate(
    target: LifecycleOwner
) : ViewDelegate<DrawerLayout>(target), DrawerListener {
    private var childRootViewReference: WeakReference<View>? = null

    var isOpened: Boolean = false
        private set

    @CallSuper
    open fun onCreate(view: DrawerLayout, childView: View, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        childRootViewReference = WeakReference(childView)
        view.addDrawerListener(this)
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("", ReplaceWith("throw UnsupportedOperationException()"))
    override fun onCreate(view: DrawerLayout, savedInstanceState: Bundle?) =
        throw UnsupportedOperationException()

    public override fun onDestroy() {
        childRootViewReference?.clear()
        view?.removeDrawerListener(this)
        super.onDestroy()
    }

    fun openDrawer() {
        childRootViewReference?.get()?.let { view?.openDrawer(it, true) }
    }

    fun closeDrawer() {
        childRootViewReference?.get()?.let { view?.closeDrawer(it, true) }
    }

    fun lockDrawer(lock: Boolean) =
        setLockMode(if (lock) LOCK_MODE_LOCKED_CLOSED else LOCK_MODE_UNLOCKED)

    protected open fun setLockMode(lockMode: Int) {
        view?.setDrawerLockMode(lockMode)
    }

    final override fun onDrawerStateChanged(newState: Int) {
    }

    final override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
    }

    final override fun onDrawerClosed(drawerView: View) {
        isOpened = false
    }

    final override fun onDrawerOpened(drawerView: View) {
        isOpened = true
    }
}