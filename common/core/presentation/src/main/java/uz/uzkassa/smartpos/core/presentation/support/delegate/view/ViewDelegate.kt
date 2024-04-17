package uz.uzkassa.smartpos.core.presentation.support.delegate.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import uz.uzkassa.smartpos.core.presentation.support.delegate.lifecycle.LifecycleDelegate
import java.lang.ref.WeakReference

@Suppress("UNUSED_PARAMETER", "unused")
open class ViewDelegate<V : View>(
    target: Any?,
    lifecycleOwner: LifecycleOwner?
) : LifecycleDelegate(lifecycleOwner) {

    constructor(activity: ComponentActivity) : this(activity, activity)

    constructor(fragment: Fragment) : this(fragment, fragment)

    constructor(lifecycleOwner: LifecycleOwner?) : this(null, lifecycleOwner)

    private val targetWeakReference: WeakReference<Any?>? = target?.let { WeakReference(it) }
    private var viewWeakReference: WeakReference<V>? = null

    @CallSuper
    open fun onCreate(view: V, savedInstanceState: Bundle?) {
        viewWeakReference = WeakReference(view)
    }

    open fun onConfigurationChanged(newConfig: Configuration?) {
    }

    open fun onSaveInstanceState(outState: Bundle?) {
    }

    @CallSuper
    override fun onDestroy() {
        targetWeakReference?.clear()
        super.onDestroy()
    }

    val view: V?
        get() = viewWeakReference?.get()

    protected val context: Context
        get() = view?.context
            ?: throw IllegalStateException("Delegate $this not attached to a context.")

    @Suppress("UNCHECKED_CAST")
    protected fun <T> getTarget(): T? =
        targetWeakReference?.get() as T?
}