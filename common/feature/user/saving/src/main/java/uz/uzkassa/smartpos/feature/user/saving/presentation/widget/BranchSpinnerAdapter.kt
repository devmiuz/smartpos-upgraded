package uz.uzkassa.smartpos.feature.user.saving.presentation.widget

import android.content.Context
import android.view.View
import android.widget.CheckedTextView
import android.widget.TextView
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.presentation.widget.spinner.DefaultBaseAdapter

internal class BranchSpinnerAdapter(context: Context) : DefaultBaseAdapter<Branch>(context) {

    override val viewLayoutId: Int
        get() = android.R.layout.simple_spinner_item

    override val dropDownLayoutId: Int
        get() = android.R.layout.simple_spinner_dropdown_item

    override fun getView(position: Int, view: View, element: Branch) {
        val textView: TextView = view.findViewById(android.R.id.text1)
        textView.text = element.name
    }

    override fun getDropDownView(position: Int, view: View, element: Branch) {
        val textView: CheckedTextView = view.findViewById(android.R.id.text1)
        textView.text = element.name
    }

    override fun getItemId(element: Branch): Long =
        element.id
}