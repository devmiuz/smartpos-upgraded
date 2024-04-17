package uz.uzkassa.smartpos.core.presentation.app.activity

import androidx.lifecycle.LifecycleOwner
import uz.uzkassa.smartpos.core.presentation.app.dispatchers.keyevent.OnKeyEventDispatcher
import uz.uzkassa.smartpos.core.presentation.support.locale.LanguageManager

interface ActivitySupport : LifecycleOwner {

    val languageManager: LanguageManager

    val onKeyEventDispatcher: OnKeyEventDispatcher
}