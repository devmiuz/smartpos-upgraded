package uz.uzkassa.smartpos.core.presentation.app.fragment.alertdialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uz.uzkassa.smartpos.core.presentation.R
import java.lang.ref.WeakReference
import kotlin.properties.Delegates

@Suppress("MemberVisibilityCanBePrivate")
class AlertDialogFragmentSupportDelegate(
    dialogFragment: AppCompatDialogFragment,
    callback: AlertDialogFragmentSupportCallback
) : DialogInterface.OnShowListener {
    private val fragmentReference: WeakReference<AppCompatDialogFragment> =
        WeakReference(dialogFragment)
    private var callbackReference: WeakReference<AlertDialogFragmentSupportCallback> =
        WeakReference(callback)

    private var alertDialog: AlertDialog by Delegates.notNull()

    private var positiveButton: Button? = null
    private var negativeButton: Button? = null
    private var neutralButton: Button? = null

    fun onCreateDialog(savedInstanceState: Bundle?, builder: AlertDialog.Builder.() -> Unit = {}) =
        onCreateDialog(null, savedInstanceState, builder)

    fun onCreateDialog(view: View, builder: AlertDialog.Builder.() -> Unit = {}) =
        onCreateDialog(view, null, builder)

    fun onCreateDialog(builder: AlertDialog.Builder.() -> Unit = {}) =
        onCreateDialog(null, null, builder)

    fun onCreateDialog(
        view: View?,
        savedInstanceState: Bundle?,
        builder: AlertDialog.Builder.() -> Unit = {}
    ): Dialog {
        fragmentReference.get()?.let {
            if (view != null) it.onViewCreated(view, savedInstanceState)

            val materialAlertDialogBuilder: AlertDialog.Builder =
                MaterialAlertDialogBuilder(it.requireContext(), R.style.Presentation_AlertDialog)
                    .also(builder)

            alertDialog =
                materialAlertDialogBuilder
                    .also(builder)
                    .also { dialog -> if (view != null) dialog.setView(view) }
                    .create()
                    .also { dialog -> dialog.setOnShowListener(this) }
        }

        return alertDialog
    }

    override fun onShow(dialog: DialogInterface?) {
        callbackReference.get()?.onAlertDialogShowing()
        if (dialog is AlertDialog) {
            positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .apply { setOnClickListener(onClickListener) }

            negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .apply { setOnClickListener(onClickListener) }

            neutralButton = dialog.getButton(DialogInterface.BUTTON_NEUTRAL)
                .apply { setOnClickListener(onClickListener) }
        }
    }

    fun setCancelable(isCancelable: Boolean) {
        fragmentReference.get()?.dialog?.apply {
            setCancelable(isCancelable)
            setCanceledOnTouchOutside(isCancelable)
        }
    }

    fun setButtonEnabled(which: Int, isEnabled: Boolean) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> positiveButton
            DialogInterface.BUTTON_NEGATIVE -> negativeButton
            else -> neutralButton
        }?.apply { this.isEnabled = isEnabled }
    }

    fun setButtonVisibility(which: Int, isVisible: Boolean) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> positiveButton
            DialogInterface.BUTTON_NEGATIVE -> negativeButton
            else -> neutralButton
        }?.apply { visibility = if (isVisible) View.VISIBLE else View.GONE }

    }

    fun setButtonText(which: Int, title: CharSequence) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> positiveButton
            DialogInterface.BUTTON_NEGATIVE -> negativeButton
            else -> neutralButton
        }?.apply { text = title }
    }

    fun setButtonText(which: Int, @StringRes resId: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> positiveButton
            DialogInterface.BUTTON_NEGATIVE -> negativeButton
            else -> neutralButton
        }?.apply { text = context?.getString(resId) }
    }

    fun setDialogCancelable(isCancelable: Boolean) {
        alertDialog.apply { setCancelable(isCancelable); setCanceledOnTouchOutside(isCancelable) }
    }

    private val onClickListener: View.OnClickListener = View.OnClickListener {
        when (it) {
            positiveButton ->
                callbackReference.get()?.onAlertDialogButtonClicked(DialogInterface.BUTTON_POSITIVE)
            negativeButton ->
                callbackReference.get()?.onAlertDialogButtonClicked(DialogInterface.BUTTON_NEGATIVE)
            neutralButton ->
                callbackReference.get()?.onAlertDialogButtonClicked(DialogInterface.BUTTON_NEUTRAL)
        }
    }
}