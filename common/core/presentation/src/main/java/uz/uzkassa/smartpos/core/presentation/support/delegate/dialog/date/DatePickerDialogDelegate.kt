package uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.date

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import uz.uzkassa.smartpos.core.presentation.support.delegate.lifecycle.LifecycleDelegate
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialogDelegate(
    private val context: Context, lifecycleOwner: LifecycleOwner?
) : LifecycleDelegate(lifecycleOwner) {

    constructor(activity: AppCompatActivity) : this(activity, activity)

    constructor(fragment: Fragment) : this(fragment.requireContext(), fragment)

    private var datePickerDialog: DatePickerDialog? = null
    private var onDateSetChanged: (date: Date) -> Unit = { }
    private val onDateSetListener: OnDateSetListener = OnDateSetListener()

    fun show(date: Date?, onDateSetChanged: (date: Date) -> Unit, onDismiss: () -> Unit = {}) {
        val triple: Triple<Int, Int, Int> = mapToTriple(date)
        datePickerDialog =
            DatePickerDialog(context, onDateSetListener, triple.first, triple.second, triple.third)
                .also {
                    it.setOnDismissListener { onDismiss() }
                    it.show()
                }

        this.onDateSetChanged = onDateSetChanged
    }

    fun dismiss() {
        datePickerDialog?.dismiss()
        datePickerDialog = null
    }

    override fun onDestroy() {
        datePickerDialog?.setOnDismissListener(null)
        datePickerDialog?.dismiss()
        datePickerDialog = null
        super.onDestroy()
    }

    private fun mapToTriple(date: Date?): Triple<Int, Int, Int> {
        val calendar: Calendar = Calendar.getInstance()
        if (date != null) calendar.time = date
        return Triple(
            first = calendar[Calendar.YEAR],
            second = calendar[Calendar.MONTH],
            third = calendar[Calendar.DAY_OF_MONTH]
        )
    }

    private inner class OnDateSetListener : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
            val date: Date = checkNotNull(dateFormat.parse("$year-${month + 1}-$dayOfMonth"))
            onDateSetChanged.invoke(date)
        }
    }
}