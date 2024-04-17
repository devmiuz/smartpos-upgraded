package uz.uzkassa.smartpos.core.presentation.app.application

import android.app.Application
import android.content.Context
import uz.uzkassa.smartpos.core.data.source.resource.language.preference.LanguagePreference
import uz.uzkassa.smartpos.core.presentation.app.callback.ApplicationLifecycle
import uz.uzkassa.smartpos.core.presentation.support.locale.LocaleManager

class ApplicationSupportDelegate(
    private val applicationSupport: ApplicationSupport,
    private val languagePreference: LanguagePreference
) {
    private val application: Application
        get() = (applicationSupport as? Application) ?: throw ClassCastException()

    fun attachBaseContext(base: Context) {
        val context: Context =
            LocaleManager.updateConfiguration(base, languagePreference.language.locale)
        applicationSupport.onSuperAttachBaseContext(context)
    }

    fun onCreate() =
        ApplicationLifecycle.registerCallbacks(application)

    fun onConfigurationChanged() =
        LocaleManager.updateConfiguration(application, languagePreference.language.locale)
}