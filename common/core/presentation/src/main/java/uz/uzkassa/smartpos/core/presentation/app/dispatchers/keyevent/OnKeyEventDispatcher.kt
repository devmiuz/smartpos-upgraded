package uz.uzkassa.smartpos.core.presentation.app.dispatchers.keyevent

import android.view.KeyEvent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import uz.uzkassa.smartpos.core.presentation.app.dispatchers.Cancellable
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

class OnKeyEventDispatcher internal constructor() {
    private val onKeyEventCallbacks: ArrayDeque<OnKeyEventCallback> = ArrayDeque()

    fun addCallback(callback: OnKeyEventCallback) {
        addCancellableCallback(callback)
    }

    private fun addCancellableCallback(callback: OnKeyEventCallback): Cancellable {
        onKeyEventCallbacks.add(callback)
        val cancellable = OnKeyEventCancellable(callback)
        callback.addCancellable(cancellable)
        return cancellable
    }

    fun addCallback(owner: LifecycleOwner, callback: OnKeyEventCallback) {
        val lifecycle: Lifecycle = owner.lifecycle
        if (lifecycle.currentState === Lifecycle.State.DESTROYED) return
        val cancelable = LifecycleCancellable(lifecycle, callback)
        callback.addCancellable(cancelable)
    }

    fun hasEnabledCallbacks(): Boolean {
        val iterator: Iterator<OnKeyEventCallback> = onKeyEventCallbacks.descendingIterator()
        while (iterator.hasNext()) {
            if (iterator.next().isEnabled) {
                return true
            }
        }
        return false
    }

    fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val iterator: Iterator<OnKeyEventCallback> = onKeyEventCallbacks.descendingIterator()

        while (iterator.hasNext()) {
            val callback: OnKeyEventCallback = iterator.next()
            if (callback.isEnabled) {
                callback.onKeyEvent(event)
                return true
            }
        }

        return false
    }

    private inner class OnKeyEventCancellable(
        private val onKeyEventCallback: OnKeyEventCallback
    ) : Cancellable {

        override fun cancel() {
            onKeyEventCallbacks.remove(onKeyEventCallback)
            onKeyEventCallback.removeCancellable(this)
        }
    }

    private inner class LifecycleCancellable internal constructor(
        private val lifecycle: Lifecycle,
        private val onKeyEventCallback: OnKeyEventCallback
    ) : LifecycleEventObserver,
        Cancellable {
        private var currentCancellable: Cancellable? = null

        init {
            lifecycle.addObserver(this)
        }

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when {
                event === Lifecycle.Event.ON_START ->
                    currentCancellable = addCancellableCallback(onKeyEventCallback)
                event === Lifecycle.Event.ON_STOP ->
                    currentCancellable?.cancel()
                event === Lifecycle.Event.ON_DESTROY -> cancel()
            }
        }

        override fun cancel() {
            lifecycle.removeObserver(this)
            onKeyEventCallback.removeCancellable(this)
            currentCancellable?.cancel()
            currentCancellable = null
        }
    }

    @Suppress("SpellCheckingInspection")
    abstract class OnKeyEventCallback(var isEnabled: Boolean) {
        private val cancellables = CopyOnWriteArrayList<Cancellable>()

        fun remove() {
            for (cancellable: Cancellable in cancellables)
                cancellable.cancel()
        }

        abstract fun onKeyEvent(event: KeyEvent?)

        internal fun addCancellable(cancellable: Cancellable) {
            cancellables.add(cancellable)
        }

        internal fun removeCancellable(cancellable: Cancellable) {
            cancellables.remove(cancellable)
        }
    }
}