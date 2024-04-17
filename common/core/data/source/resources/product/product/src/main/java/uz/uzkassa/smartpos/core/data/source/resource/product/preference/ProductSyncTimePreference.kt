package uz.uzkassa.smartpos.core.data.source.resource.product.preference

import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource
import java.util.*

interface ProductSyncTimePreference {

    val lastSyncTime: String?

    fun setLastSyncTime(date: Date)

    companion object {

        fun instantiate(preferenceSource: PreferenceSource): ProductSyncTimePreference =
            ProductSyncTimePreferenceImpl(preferenceSource)
    }
}