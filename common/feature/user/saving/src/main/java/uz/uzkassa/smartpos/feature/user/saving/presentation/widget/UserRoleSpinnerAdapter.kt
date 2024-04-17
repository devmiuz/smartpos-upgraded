package uz.uzkassa.smartpos.feature.user.saving.presentation.widget

import android.content.Context
import android.view.View
import android.widget.CheckedTextView
import android.widget.TextView
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.presentation.widget.spinner.DefaultBaseAdapter

internal class UserRoleSpinnerAdapter(context: Context) : DefaultBaseAdapter<UserRole>(context) {

    override val viewLayoutId: Int
        get() = android.R.layout.simple_spinner_item

    override val dropDownLayoutId: Int
        get() = android.R.layout.simple_spinner_dropdown_item

    override fun getView(position: Int, view: View, element: UserRole) {
        val textView: TextView = view.findViewById(android.R.id.text1)
        textView.text = element.nameRu
    }

    override fun getDropDownView(position: Int, view: View, element: UserRole) {
        val textView: CheckedTextView = view.findViewById(android.R.id.text1)
        textView.text = element.nameRu
    }

    override fun getItemId(element: UserRole): Long =
        element.type.ordinal.toLong()
}