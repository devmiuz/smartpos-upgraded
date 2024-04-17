package uz.uzkassa.smartpos.core.presentation.app.fragment.dialog

import android.app.Dialog
import android.view.Gravity
import android.view.Window
import android.view.WindowManager.LayoutParams
import androidx.fragment.app.DialogFragment
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.presentation.support.delegate.lifecycle.LifecycleDelegate
import java.lang.ref.WeakReference

open class DialogFragmentSupportDelegate(
    dialogFragment: DialogFragment
) : LifecycleDelegate(dialogFragment) {
    private val reference: WeakReference<DialogFragment> = WeakReference(dialogFragment)
    private var layoutParamsAction: ((layoutParams: LayoutParams) -> Unit) = {}

    private val dialog: Dialog?
        get() = reference.get()?.dialog

    fun onCreateDialog(dialog: Dialog.() -> Unit = {}): Dialog =
        checkNotNull(reference.get()).let { it ->
            object : Dialog(it.requireContext(), R.style.Presentation_Dialog) {

            }.also { it.window?.requestFeature(Window.FEATURE_NO_TITLE) }.also(dialog)
        }

    fun setDialogWindowParams(layoutParams: (LayoutParams) -> Unit) {
        layoutParamsAction = layoutParams
    }

    override fun onResume() {
        dialog?.window?.attributes = getDefaultWindowLayoutParams()?.apply(layoutParamsAction)
    }

    fun setDialogCancelable(isCancelable: Boolean) {
        reference.get()?.isCancelable = isCancelable
    }

    private fun getDefaultWindowLayoutParams(): LayoutParams? =
        dialog?.window?.attributes?.apply {
            width = LayoutParams.MATCH_PARENT
            height = LayoutParams.MATCH_PARENT
            gravity = Gravity.CENTER
        }
}