package uz.uzkassa.smartpos.core.data.source.resource.category.preference

import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource
import java.util.*

interface CategorySyncTimePreference {

    val lastSyncTime: String?

    fun setLastSyncTime(date: Date)

    companion object {

        fun instantiate(preferenceSource: PreferenceSource): CategorySyncTimePreference =
            CategorySyncTimePreferenceImpl(preferenceSource)
    }
}