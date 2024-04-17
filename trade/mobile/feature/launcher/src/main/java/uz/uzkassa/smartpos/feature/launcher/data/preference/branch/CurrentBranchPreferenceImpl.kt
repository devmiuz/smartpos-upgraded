package uz.uzkassa.smartpos.feature.launcher.data.preference.branch

import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource

internal class CurrentBranchPreferenceImpl(
    private val preferenceSource: PreferenceSource
) : CurrentBranchPreference {

    override var branchId: Long?
        get() = preferenceSource.getLong(PREFERENCE_LONG_BRANCH_ID, Long.MIN_VALUE)
            .let { if (it == Long.MIN_VALUE) null else it }
        set(value) {
            value?.let { preferenceSource.setLong(PREFERENCE_LONG_BRANCH_ID, it) }
        }

    private companion object {
        const val PREFERENCE_LONG_BRANCH_ID: String = "preference_long_branch_id"
    }
}