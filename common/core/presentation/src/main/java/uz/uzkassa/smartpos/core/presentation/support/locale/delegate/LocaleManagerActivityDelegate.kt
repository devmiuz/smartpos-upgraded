package uz.uzkassa.smartpos.core.presentation.support.locale.delegate

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.View
import androidx.activity.ComponentActivity
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.data.source.resource.language.preference.LanguagePreference
import uz.uzkassa.smartpos.core.presentation.app.activity.ActivitySupport
import uz.uzkassa.smartpos.core.presentation.support.delegate.lifecycle.LifecycleDelegate
import uz.uzkassa.smartpos.core.presentation.support.locale.LocaleManager
import java.util.*

internal class LocaleManagerActivityDelegate(
    activitySupport: ActivitySupport,
    private val languagePreference: LanguagePreference
) : LifecycleDelegate(activitySupport) {
    private val rightToLeftLocaleCodes: List<String> by lazy {
        listOf("ar", "dv", "fa", "ha", "he", "iw", "ji", "ps", "sd", "ug", "ur", "yi")
    }

    private var locale: Locale = Locale.getDefault()
    private val activity: ComponentActivity
        get() = getLifecycleOwner() as ComponentActivity

    override fun onCreate() {
        activity.window.decorView.layoutDirection =
            if (rightToLeftLocaleCodes.contains(Locale.getDefault().language)) View.LAYOUT_DIRECTION_RTL
            else View.LAYOUT_DIRECTION_LTR
    }

    fun applyOverrideConfiguration(
        baseContext: Context,
        overrideConfiguration: Configuration?
    ): Configuration? {
        if (overrideConfiguration != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            val uiMode: Int = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        return overrideConfiguration
    }

    override fun onPause() {
        locale = Locale.getDefault()
    }

    override fun onResume() {
        if (locale == Locale.getDefault()) return
        activity.recreate()
    }

    fun changeLanguage(language: Language) {
        LocaleManager.updateConfiguration(activity, language.locale)
        languagePreference.language = language
        locale = language.locale
    }
}