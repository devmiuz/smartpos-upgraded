package uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import uz.uzkassa.smartpos.core.presentation.utils.app.getArgument
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments

class ErrorDialogFragment internal constructor() : DialogFragment() {
    private val delegate by lazy { ErrorDialogDelegate(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.setThrowable(throwable)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        delegate.getDialogInstance()

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val onErrorDialogDismissListener: OnErrorDialogDismissListener? = when {
            parentFragment != null -> parentFragment as? OnErrorDialogDismissListener
            else -> activity as? OnErrorDialogDismissListener
        }
        onErrorDialogDismissListener?.onErrorDialogDismissed(throwable)
    }

    private val throwable: Throwable? by lazy {
        getArgument<Throwable>(BUNDLE_SERIALIZABLE_THROWABLE)
    }

    companion object {
        private const val BUNDLE_SERIALIZABLE_THROWABLE: String = "bundle_serializable_throwable"

        fun show(fragment: Fragment, throwable: Throwable?) =
            ErrorDialogFragment()
                .withArguments {
                    if (throwable != null)
                        putSerializable(BUNDLE_SERIALIZABLE_THROWABLE, throwable)
                }
                .show(fragment.childFragmentManager, ErrorDialogFragment::class.java.name)

        fun show(fragment: FragmentManager, throwable: Throwable?) =
            ErrorDialogFragment()
                .withArguments {
                    if (throwable != null)
                        putSerializable(BUNDLE_SERIALIZABLE_THROWABLE, throwable)
                }
                .show(fragment, ErrorDialogFragment::class.java.name)
    }
}