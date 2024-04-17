package uz.uzkassa.smartpos.core.presentation.utils.widget

import android.text.InputFilter
import android.widget.EditText

fun EditText.addInputFilter(filter: InputFilter) {
    filters = filters.toMutableList().apply { add(filter) }.toTypedArray()
}

fun EditText.removeInputFilter(filter: InputFilter) {
    filters = filters.toMutableList().apply { remove(filter) }.toTypedArray()
}