package uz.uzkassa.smartpos.feature.company.saving.presentation.widget

import android.content.Context
import android.view.View
import android.widget.CheckedTextView
import android.widget.TextView
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType
import uz.uzkassa.smartpos.core.presentation.widget.spinner.DefaultBaseAdapter

internal class CompanyBusinessTypeAdapter(
    context: Context
) : DefaultBaseAdapter<CompanyBusinessType>(context) {

    override val viewLayoutId: Int
        get() = android.R.layout.simple_spinner_item

    override val dropDownLayoutId: Int
        get() = android.R.layout.simple_spinner_dropdown_item

    override fun getView(position: Int, view: View, element: CompanyBusinessType) {
        val textView: TextView = view.findViewById(android.R.id.text1)
        textView.text = element.nameRu
    }

    override fun getDropDownView(position: Int, view: View, element: CompanyBusinessType) {
        val textView: CheckedTextView = view.findViewById(android.R.id.text1)
        textView.text = element.nameRu
    }

    override fun getItemId(element: CompanyBusinessType): Long =
        element.hashCode().toLong()
}