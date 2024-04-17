package uz.uzkassa.smartpos.core.presentation.support.delegate.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import uz.uzkassa.smartpos.core.presentation.support.delegate.lifecycle.LifecycleDelegate
import java.lang.ref.WeakReference

abstract class BroadcastReceiverDelegate(
    lifecycleOwner: LifecycleOwner?, context: Context?
) : LifecycleDelegate(lifecycleOwner) {

    constructor(activity: AppCompatActivity) : this(activity, activity)

    private val contextReference: WeakReference<Context?> = WeakReference(context)
    private var isRegistered: Boolean = false
    private lateinit var receiver: BroadcastReceiver

    init {
        init()
    }

    fun init() {
        receiver = getBroadcastReceiver()
    }

    @CallSuper
    open fun registerReceiver() {
        if (!isRegistered) {
            context?.registerReceiver(receiver, getIntentFilter())
            isRegistered = true
        }
    }

    @CallSuper
    open fun registerReceiver(flags: Int) {
        if (!isRegistered && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.registerReceiver(receiver, getIntentFilter(), flags)
            isRegistered = true
        }
    }

    @CallSuper
    open fun registerReceiver(broadcastPermission: String?, scheduler: Handler?) {
        if (!isRegistered && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.registerReceiver(receiver, getIntentFilter(), broadcastPermission, scheduler)
            isRegistered = true
        }
    }

    @CallSuper
    open fun registerReceiver(
        intentFilter: IntentFilter, broadcastPermission: String?,
        scheduler: Handler?, flags: Int
    ) {
        if (isRegistered && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.registerReceiver(receiver, intentFilter, broadcastPermission, scheduler, flags)
            isRegistered = true
        }
    }

    @CallSuper
    open fun unregisterReceiver() {
        if (isRegistered) context?.unregisterReceiver(receiver)
        isRegistered = false
    }

    protected val context: Context? = contextReference.get()

    protected abstract fun getBroadcastReceiver(): BroadcastReceiver

    protected open fun getIntentFilter(): IntentFilter = IntentFilter()
}