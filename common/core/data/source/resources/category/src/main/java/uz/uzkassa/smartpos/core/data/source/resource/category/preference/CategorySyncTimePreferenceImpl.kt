package uz.uzkassa.smartpos.core.data.source.resource.category.preference

import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

internal class CategorySyncTimePreferenceImpl(
    private val preferenceSource: PreferenceSource
) : CategorySyncTimePreference {
    private val dateFormat: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    override val lastSyncTime: String?
        get() = preferenceSource
            .getString(PREFERENCE_STRING_LAST_SYNC_TIME, "")
            .let { return@let if (it.isBlank()) null else it }

    override fun setLastSyncTime(date: Date) =
        preferenceSource.setString(PREFERENCE_STRING_LAST_SYNC_TIME, dateFormat.format(date))

    companion object {
        const val PREFERENCE_STRING_LAST_SYNC_TIME: String = "preference_string_last_sync_time"
    }
}