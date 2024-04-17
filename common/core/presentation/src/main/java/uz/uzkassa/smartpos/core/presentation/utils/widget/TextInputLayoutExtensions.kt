package uz.uzkassa.smartpos.core.presentation.utils.widget

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.google.android.material.internal.CheckableImageButton
import com.google.android.material.textfield.TextInputLayout
import uz.uzkassa.smartpos.core.presentation.widget.textinputlayout.OnPasswordVisibilityToggleListener

@SuppressLint("RestrictedApi")
fun TextInputLayout.setOnPasswordVisibilityToggleListener(
    listener: OnPasswordVisibilityToggleListener
) = findTogglePasswordButton(this)?.let {
    it.setOnTouchListener { view, motionEvent ->
        if (motionEvent.action == MotionEvent.ACTION_UP)
            listener.onPasswordVisibilityToggle(this, (view as CheckableImageButton).isChecked)
        return@setOnTouchListener false
    }
}

fun TextInputLayout.setOnPasswordVisibilityToggle(
    action: (Boolean) -> Unit
) = findTogglePasswordButton(this)?.let {
    val listener = object : OnPasswordVisibilityToggleListener {
        override fun onPasswordVisibilityToggle(
            textInputLayout: TextInputLayout,
            isVisible: Boolean
        ) = action.invoke(isVisible)

    }
    return@let setOnPasswordVisibilityToggleListener(listener)
}

@SuppressLint("RestrictedApi")
fun TextInputLayout.togglePasswordVisibility() {
    findTogglePasswordButton(this)?.performClick()
}

private fun findTogglePasswordButton(viewGroup: ViewGroup): CheckableImageButton? {
    val childCount: Int = viewGroup.childCount
    for (ind: Int in 0 until childCount) {
        val child: View = viewGroup.getChildAt(ind)
        if (child is ViewGroup) {
            val togglePasswordButton: CheckableImageButton? = findTogglePasswordButton(child)
            if (togglePasswordButton != null) return togglePasswordButton
        } else if (child is CheckableImageButton) return child
    }

    return null
}