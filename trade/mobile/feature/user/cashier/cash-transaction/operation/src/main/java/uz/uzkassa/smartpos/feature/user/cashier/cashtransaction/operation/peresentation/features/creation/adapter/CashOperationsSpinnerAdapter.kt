package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.creation.adapter

import android.content.Context
import android.view.View
import android.widget.CheckedTextView
import android.widget.TextView
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.presentation.widget.spinner.DefaultBaseAdapter
import uz.uzkassa.smartpos.core.utils.resource.string.get

internal class CashOperationsSpinnerAdapter(
    context: Context
) : DefaultBaseAdapter<CashOperation>(context) {

    override val viewLayoutId: Int
        get() = android.R.layout.simple_spinner_item

    override val dropDownLayoutId: Int
        get() = android.R.layout.simple_spinner_dropdown_item

    override fun getView(position: Int, view: View, element: CashOperation) {
        val textView: TextView = view.findViewById(android.R.id.text1)
        textView.text = element.resourceString.get(view.context)
    }

    override fun getDropDownView(position: Int, view: View, element: CashOperation) {
        val textView: CheckedTextView = view.findViewById(android.R.id.text1)
        textView.text = element.resourceString.get(view.context)
    }

    override fun getItemId(element: CashOperation): Long =
        element.hashCode().toLong()
}