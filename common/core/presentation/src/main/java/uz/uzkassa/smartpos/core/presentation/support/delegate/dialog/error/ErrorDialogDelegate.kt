package uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.presentation.constants.GlobalConstants
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.error.ErrorContent
import uz.uzkassa.smartpos.core.presentation.support.error.asErrorContent

class ErrorDialogDelegate(
    context: Context,
    lifecycleOwner: LifecycleOwner?
) : AlertDialogDelegate(context, lifecycleOwner) {

    constructor(activity: ComponentActivity) : this(activity, activity)

    constructor(fragment: Fragment) : this(fragment.requireContext(), fragment)

    private var isInitialized: Boolean = false

    private val messageTextView: AppCompatTextView
    private val dividerView: View
    private val typeTextView: AppCompatTextView
    private val detailsTextView: AppCompatTextView
    private val codeTextView: AppCompatTextView
    private val appVersionTextView: AppCompatTextView
    private val osVersionTextView: AppCompatTextView

    init {
        @SuppressLint("InflateParams")
        val viewGroup: ViewGroup =
            LayoutInflater.from(context)
                .inflate(R.layout.core_presentation_layout_error_dialog, null) as ViewGroup

        messageTextView = viewGroup.getChildAt(0) as AppCompatTextView
        dividerView = viewGroup.getChildAt(1)
        typeTextView = viewGroup.getChildAt(2) as AppCompatTextView
        detailsTextView = viewGroup.getChildAt(3) as AppCompatTextView
        codeTextView = viewGroup.getChildAt(4) as AppCompatTextView
        appVersionTextView = viewGroup.getChildAt(5) as AppCompatTextView
        osVersionTextView = viewGroup.getChildAt(6) as AppCompatTextView

        @Suppress("DEPRECATION")
        newBuilder {
            setTitle(R.string.core_presentation_common_alert)
            setView(viewGroup)
            setPositiveButton(R.string.core_presentation_common_ok) { _, _ -> dismiss() }
        }

        isInitialized = true
    }

    fun setThrowable(throwable: Throwable?) {
        val errorDetails: ErrorContent = throwable.asErrorContent(checkNotNull(context))
        messageTextView.text = errorDetails.message

        setText(
            textView = typeTextView,
            text = errorDetails.type?.let {
                getString(R.string.core_presentation_layout_error_dialog_type, it)
            }
        )

        setText(
            textView = detailsTextView,
            text = errorDetails.details?.let {
                getString(R.string.core_presentation_layout_error_dialog_details, it)
            }
        )

        setText(
            textView = codeTextView,
            text = errorDetails.code?.let {
                getString(R.string.core_presentation_layout_error_dialog_code, it)
            }
        )

        setText(
            textView = appVersionTextView,
            text = context.getString(
                R.string.core_presentation_app_version,
                GlobalConstants.appVersion
            )
        )

        setText(
            textView = osVersionTextView,
            text =  context.getString(
                R.string.core_presentation_os_version,
                android.os.Build.VERSION.RELEASE
            )
        )

//        if (errorDetails.isNullContent)
//            dividerView.visibility = View.GONE
    }

    fun show(throwable: Throwable?) {
        setThrowable(throwable)
        super.show()
    }

    fun getDialogInstance(): Dialog =
        instance

    @Deprecated("", ReplaceWith("throw UnsupportedOperationException()"))
    override fun dialog(dialog: AlertDialog.() -> Unit) =
        throw UnsupportedOperationException()

    @Deprecated("")
    override fun newBuilder(builder: AlertDialog.Builder.() -> Unit) {
        if (!isInitialized) super.newBuilder(builder)
        else throw UnsupportedOperationException()
    }

    private fun setText(textView: AppCompatTextView, text: CharSequence?) {
        if (text == null) textView.visibility = View.GONE
        else textView.text = text
    }

    private fun getString(@StringRes resId: Int, vararg args: Any): CharSequence =
        checkNotNull(context).getString(resId, *args)
}