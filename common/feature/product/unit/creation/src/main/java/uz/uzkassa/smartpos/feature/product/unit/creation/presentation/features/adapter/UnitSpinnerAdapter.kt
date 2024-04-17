package uz.uzkassa.smartpos.feature.product.unit.creation.presentation.features.adapter

import android.content.Context
import android.view.View
import android.widget.CheckedTextView
import android.widget.TextView
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.presentation.widget.spinner.DefaultBaseAdapter

internal class UnitSpinnerAdapter(
    context: Context
) : DefaultBaseAdapter<Unit>(context) {

    override val viewLayoutId: Int
        get() = android.R.layout.simple_spinner_item

    override val dropDownLayoutId: Int
        get() = android.R.layout.simple_spinner_dropdown_item

    override fun getView(position: Int, view: View, element: Unit) {
        val textView: TextView = view.findViewById(android.R.id.text1)
        textView.text = element.name
    }

    override fun getDropDownView(position: Int, view: View, element: Unit) {
        val textView: CheckedTextView = view.findViewById(android.R.id.text1)
        textView.text = element.name
    }

    override fun getItemId(element: Unit): Long =
        element.id
}