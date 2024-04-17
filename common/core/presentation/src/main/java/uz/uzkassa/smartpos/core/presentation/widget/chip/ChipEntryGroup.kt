package uz.uzkassa.smartpos.core.presentation.widget.chip

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import uz.uzkassa.smartpos.core.presentation.R

class ChipEntryGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ChipGroup(context, attrs, defStyleAttr) {

    init {
        if (isInEditMode) {
            addEntry(0, "First Chip Entry") {}
            addEntry(1, "Second Chip Entry") {}
            addEntry(2, "Third Chip Entry") {}
        }
    }

    fun addEntry(id: Int, text: String, closeListener: () -> Unit) {
        addView(
            (View.inflate(context, R.layout.widget_chip_entry, null) as Chip).apply {
                this.id = id
                this.text = text
                setOnCloseIconClickListener { closeListener.invoke() }
            }
        )
    }

    fun <T> addEntries(
        list: List<T>, id: (T) -> Int,
        text: (T) -> String, closeListener: (T) -> Unit
    ) = list.forEach { addEntry(id.invoke(it), text.invoke(it)) { closeListener.invoke(it) } }

    fun removeEntry(id: Int) =
        removeView(findViewById(id))

    fun removeEntries() =
        removeAllViews()
}