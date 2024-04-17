package uz.uzkassa.smartpos.core.presentation.support.delegate.lifecycle

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference

open class LifecycleDelegate(lifecycleOwner: LifecycleOwner?) : LifecycleObserver {
    private val lifecycleReference: WeakReference<LifecycleOwner?> = WeakReference(lifecycleOwner)

    init {
        init()
    }

    private fun init() {
        lifecycleReference.get()?.lifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected open fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun onStop() {
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun onDestroy() {
        lifecycleReference.let {
            it.get()?.lifecycle?.removeObserver(this)
            it.clear()
        }
    }

    protected fun getLifecycleOwner(): LifecycleOwner? =
        lifecycleReference.get()
}