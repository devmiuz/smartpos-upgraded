package uz.uzkassa.smartpos.core.data.source.preference.cleaner

import uz.uzkassa.smartpos.core.data.source.preference.manager.PreferenceManager

class PreferenceCleanerImpl(private val preferenceManager: PreferenceManager) : PreferenceCleaner {

    override fun clearAll() {
        preferenceManager.clearAll()
    }

}
