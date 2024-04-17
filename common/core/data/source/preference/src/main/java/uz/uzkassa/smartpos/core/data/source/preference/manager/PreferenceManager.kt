package uz.uzkassa.smartpos.core.data.source.preference.manager

import android.content.Context
import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource

interface PreferenceManager {

    fun create(preferenceName: String): PreferenceSource

    fun clearAll()

    companion object {

        fun instantiate(context: Context): PreferenceManager =
            PreferenceManagerImpl(context)
    }
}