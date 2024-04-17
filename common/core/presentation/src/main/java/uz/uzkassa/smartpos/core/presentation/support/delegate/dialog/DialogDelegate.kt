package uz.uzkassa.smartpos.core.presentation.support.delegate.dialog

import android.app.Dialog
import android.content.Context
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import uz.uzkassa.smartpos.core.presentation.support.delegate.lifecycle.LifecycleDelegate
import java.lang.ref.WeakReference

abstract class DialogDelegate<T : Dialog> constructor(
    context: Context, lifecycleOwner: LifecycleOwner?
) : LifecycleDelegate(lifecycleOwner) {

    constructor(activity: AppCompatActivity) : this(activity.baseContext, activity)

    constructor(fragment: Fragment) : this(fragment.requireContext(), fragment)

    private val contextReference: WeakReference<Context> = WeakReference(context)
    private var dialog: T? = null

    protected val context: Context
        get() = contextReference.get()
            ?: throw KotlinNullPointerException("Context must be not null")

    protected val instance: T
        get() = dialog ?: throw KotlinNullPointerException()

    init {
        init()
    }

    open fun dialog(dialog: T.() -> Unit) {
        this.dialog?.apply(dialog)
    }

    open fun show() {
        dialog?.show()
    }

    open fun dismiss() {
        dialog?.dismiss()
    }

    @CallSuper
    override fun onDestroy() {
        dialog?.let { it.setOnCancelListener(null); it.setOnDismissListener(null) }
        dialog?.let {
            if (it.isShowing) {
                it.cancel()
                it.dismiss()
            }
        }
        dialog = null
        super.onDestroy()
    }

    private fun init() {
        dialog = getDialog()
    }

    protected fun setDialog(dialog: T) {
        this.dialog = dialog
    }

    protected abstract fun getDialog(): T
}