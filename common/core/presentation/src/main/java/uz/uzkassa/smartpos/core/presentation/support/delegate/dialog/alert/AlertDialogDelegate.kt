package uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.DialogDelegate

open class AlertDialogDelegate constructor(
    context: Context, lifecycleOwner: LifecycleOwner?
) : DialogDelegate<AlertDialog>(context, lifecycleOwner) {

    constructor(activity: AppCompatActivity) : this(activity, activity)

    constructor(fragment: Fragment) : this(fragment.requireContext(), fragment)

    override fun getDialog(): AlertDialog =
        getAlertDialogBuilder().create()

    open fun newBuilder(builder: AlertDialog.Builder.() -> Unit) =
        setDialog(getAlertDialogBuilder().apply(builder).create())

    private fun getAlertDialogBuilder(): AlertDialog.Builder =
        MaterialAlertDialogBuilder(context, R.style.Presentation_AlertDialog)
}