package uz.uzkassa.smartpos.core.data.source.preference.manager

import android.content.Context
import android.content.SharedPreferences
import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource
import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSourceImpl

internal class PreferenceManagerImpl(private val context: Context) : PreferenceManager {
    private val sources: MutableList<PreferenceSource> = arrayListOf()

    override fun create(preferenceName: String): PreferenceSource {
        val source: PreferenceSource = PreferenceSourceImpl(getSharedPreferences(preferenceName))
        sources.add(source)
        return source
    }

    override fun clearAll() =
        sources.forEach { it.clearAll() }

    private fun getSharedPreferences(preferenceName: String): SharedPreferences =
        context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
}