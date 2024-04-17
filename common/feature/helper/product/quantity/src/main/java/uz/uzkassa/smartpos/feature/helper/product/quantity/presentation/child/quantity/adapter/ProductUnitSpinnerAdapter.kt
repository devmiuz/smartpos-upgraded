package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.quantity.adapter

import android.content.Context
import android.view.View
import android.widget.CheckedTextView
import android.widget.TextView
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.presentation.widget.spinner.DefaultBaseAdapter

internal class ProductUnitSpinnerAdapter(
    context: Context
) : DefaultBaseAdapter<ProductUnit>(context) {

    override val viewLayoutId: Int
        get() = android.R.layout.simple_spinner_item

    override val dropDownLayoutId: Int
        get() = android.R.layout.simple_spinner_dropdown_item

    override fun getView(position: Int, view: View, element: ProductUnit) {
        val textView: TextView = view.findViewById(android.R.id.text1)
        textView.visibility = View.GONE
    }

    override fun getDropDownView(position: Int, view: View, element: ProductUnit) {
        val textView: CheckedTextView = view.findViewById(android.R.id.text1)
        textView.text = element.unit.description
    }

    override fun getItemId(element: ProductUnit): Long =
        element.hashCode().toLong()
}