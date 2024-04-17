package uz.uzkassa.smartpos.core.presentation.utils.app

import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import uz.uzkassa.smartpos.core.presentation.app.activity.ActivitySupport
import uz.uzkassa.smartpos.core.presentation.app.dispatchers.keyevent.OnKeyEventDispatcher
import uz.uzkassa.smartpos.core.presentation.support.locale.LanguageManager
import java.io.Serializable

val Fragment.onBackPressedDispatcher: OnBackPressedDispatcher
    get() = requireActivity().onBackPressedDispatcher

val Fragment.onKeyEventDispatcher: OnKeyEventDispatcher
    get() = (requireActivity() as ActivitySupport).onKeyEventDispatcher

val Fragment.languageManager: LanguageManager
    get() = requireActivity().languageManager

fun Fragment.showSoftInput() =
    requireActivity().showSoftInput()

fun Fragment.hideSoftInput() =
    requireActivity().hideSoftInput()

inline fun <reified V> Fragment.getArgument(key: String): V? =
    arguments?.get(key).let {
        return@let when (it) {
            is V -> it
            is Serializable -> it as? V
            else -> null
        }
    }

@Suppress("SpellCheckingInspection")
inline fun <reified V : Any> Fragment.getNonNullArgument(key: String): V =
    checkNotNull(getArgument<V>(key))

inline fun <T : Fragment> T.withArguments(bundle: Bundle. () -> Unit = {}): T =
    apply { arguments = Bundle().apply(bundle) }