package uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate

class LoadingDialogDelegate(
    context: Context, lifecycleOwner: LifecycleOwner?
) : AlertDialogDelegate(context, lifecycleOwner) {

    constructor(activity: AppCompatActivity) : this(activity, activity)

    constructor(fragment: Fragment) : this(fragment.requireContext(), fragment)

    private var isInitialized: Boolean = false

    init {
        @Suppress("DEPRECATION")
        newBuilder {
            setView(R.layout.core_presentation_layout_dialog_loading)
            setCancelable(false)
        }

        isInitialized = true
    }

    @Deprecated("", ReplaceWith("throw UnsupportedOperationException()"))
    override fun dialog(dialog: AlertDialog.() -> Unit) =
        throw UnsupportedOperationException()

    @Deprecated("")
    override fun newBuilder(builder: AlertDialog.Builder.() -> Unit) {
        if (!isInitialized) super.newBuilder(builder)
        else throw UnsupportedOperationException()
    }
}