package uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers

import android.view.KeyEvent
import androidx.lifecycle.LifecycleOwner
import uz.uzkassa.smartpos.core.presentation.app.dispatchers.keyevent.OnKeyEventDispatcher

fun OnKeyEventDispatcher.addCallback(
    owner: LifecycleOwner? = null,
    enabled: Boolean = true,
    callback: (KeyEvent?) -> Unit
): OnKeyEventDispatcher.OnKeyEventCallback {
    val eventCallback = object : OnKeyEventDispatcher.OnKeyEventCallback(enabled) {
        override fun onKeyEvent(event: KeyEvent?) = callback.invoke(event)
    }
    if (owner != null) addCallback(owner, eventCallback)
    else addCallback(eventCallback)
    return eventCallback
}