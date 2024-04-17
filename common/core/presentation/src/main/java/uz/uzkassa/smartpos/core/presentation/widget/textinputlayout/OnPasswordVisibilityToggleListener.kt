package uz.uzkassa.smartpos.core.presentation.widget.textinputlayout

import com.google.android.material.textfield.TextInputLayout

interface OnPasswordVisibilityToggleListener {
    fun onPasswordVisibilityToggle(textInputLayout: TextInputLayout, isVisible: Boolean)
}