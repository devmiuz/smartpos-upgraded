package uz.uzkassa.smartpos.core.presentation.support.locale

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*

internal object LocaleManager {

    @Suppress("DEPRECATION")
    fun updateConfiguration(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setLayoutDirection(locale)
        configuration.setLocale(locale)
        return resources.updateConfiguration(configuration, resources.displayMetrics).let { context }
    }
}