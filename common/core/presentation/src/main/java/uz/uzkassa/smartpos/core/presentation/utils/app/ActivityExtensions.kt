package uz.uzkassa.smartpos.core.presentation.utils.app

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import uz.uzkassa.smartpos.core.presentation.app.activity.ActivitySupport
import uz.uzkassa.smartpos.core.presentation.support.locale.LanguageManager

val Activity.languageManager: LanguageManager
    get() = (this as ActivitySupport).languageManager

fun Activity.showSoftInput() {
    if (currentFocus != null)
        getInputMethodManager(this).showSoftInput(currentFocus, 0)
}

fun Activity.hideSoftInput() {
    if (currentFocus != null)
        getInputMethodManager(this)
            .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

private fun getInputMethodManager(context: Context): InputMethodManager =
    context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager