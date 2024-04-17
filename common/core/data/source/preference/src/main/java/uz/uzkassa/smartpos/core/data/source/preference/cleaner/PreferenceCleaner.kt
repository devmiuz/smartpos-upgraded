package uz.uzkassa.smartpos.core.data.source.preference.cleaner

import uz.uzkassa.smartpos.core.data.source.preference.manager.PreferenceManager

interface PreferenceCleaner {

    fun clearAll()

    companion object {

        fun instantiate(preferenceManager: PreferenceManager): PreferenceCleaner =
            PreferenceCleanerImpl(preferenceManager)
    }
}
